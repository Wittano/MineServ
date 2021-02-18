import asyncio
import os
import shutil
import signal
import subprocess
from typing import List, Optional

from django.contrib.auth.models import User
from apps.server.models import Server, Version
from apps.server.utils.download import ServerDownloader, exist
from mineserv.property import DOWNLOAD_DIR
from .properites import template


class MinecraftServer:
    """Manager for minecraft server

    Raises:
        DoesNotExist: raise when version, which was given, be won't found in database
    """

    def __init__(self, name: str, version: str):
        if Version.objects.get(version=version):
            self._VERSION = version
        self._NAME = name
        self._MC = ServerDownloader(self._VERSION)
        self._PATH = f"{DOWNLOAD_DIR}/{self._NAME}"

    def _process(self, args: Optional[List[str]] = None) -> subprocess.Popen:
        """Run server minecraft with special arguments

        Args:
            args: arguments, which will be used when the server is stared

        Returns:
            Information about minecraft server process
        """
        if args is None:
            args = []

        pwd = os.getcwd()
        command = ["java", "-jar", f"minecraft-server-{self._VERSION}.jar"]
        command.extend(args)

        os.chdir(self._PATH)
        p = subprocess.Popen(command)
        os.chdir(pwd)

        return p

    def _init(self) -> subprocess.Popen:
        """Initialized minecraft server"""
        return self._process(["--initSettings", "--nogui"])

    def _run(self) -> subprocess.Popen:
        """Run minecraft server"""
        return self._process(["--nogui"])

    def create(self, user: Optional[User] = None) -> None:
        """

        Raises:
            Version.DoesNotExist: raise when version wasn't found
            ValueError: raise when version had wrong format
        """
        if not os.path.isdir(self._PATH):
            os.mkdir(self._PATH)

        if not exist(self._PATH, self._VERSION):
            asyncio.run(self._MC.download(self._PATH))

        p = self._init()

        if Version(version=self._VERSION) > "1.7.9":
            p.wait()
        else:
            try:
                p.wait(3)
            except subprocess.TimeoutExpired:
                p.kill()

        template(self._PATH, self._VERSION)

        server = Server(
            version=Version.objects.get(version=self._VERSION), name=self._NAME, user=user
        )
        server.save()

    def start(self, id: int) -> subprocess.Popen:
        """Run server minecraft

        Args:
            id: server id, which will be run

        Raises:
            Server.DoesNotExist: raise when server wasn't found

        Returns:
            Information about minecraft server process
        """
        server = Server.objects.get(id=id)

        p = self._run()
        server.pid = p.pid
        server.status = 2
        server.save()

        return p

    @staticmethod
    def stop(id: int) -> None:
        """Stop server

        Args:
            id: server id

        Raises:
            Server.DoesNotExist: raise when server wasn't found
        """
        server = Server.objects.get(id=id)

        os.kill(server.pid, signal.SIGKILL)
        server.pid = None
        server.status = 1
        server.save()

    def delete(self, id: int) -> None:
        """Removed server from server and database

        Args:
            id: server id

        Raises:
            Server.DoesNotExist: raise when server wasn't found
        """
        server = Server.objects.get(id=id)
        if server.status != 1:
            self.stop(id)

        server.delete()
        shutil.rmtree(self._PATH)
