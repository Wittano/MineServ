import asyncio
import os
import subprocess

from apps.server.models import Server, Version
from mineserv.property import DOWNLOAD_DIR
from .download import ServerDownloader, exist
from .properites import template


class MinecraftServer:
    """

    Raises:
        DoesNotExist: raise when version, which was given, be won't found in database
    """

    def __init__(self, name: str, version):
        if Version.objects.get(version=version):
            self._VERSION = version
        self._NAME = name
        self._MC = ServerDownloader(self._VERSION)
        self._PATH = f"{DOWNLOAD_DIR}/{self._NAME}"

    def create(self) -> None:
        """

        Raises:
            DoesNotExist: raise when version wasn't found

        """
        if not os.path.isdir(self._PATH):
            os.mkdir(self._PATH)

        if not exist(self._PATH, self._VERSION):
            asyncio.run(self._MC.download(self._PATH))

        pwd = os.getcwd()

        os.chdir(self._PATH)
        p = subprocess.Popen(
            [
                "java",
                "-jar",
                f"minecraft-server-{self._VERSION}.jar",
                "--initSettings",
                "--nogui",
            ]
        )
        os.chdir(pwd)

        if Version(version=self._VERSION) > "1.7.9":
            p.wait()
        else:
            try:
                p.wait(3)
            except subprocess.TimeoutExpired:
                p.kill()

        template(self._PATH, self._VERSION)

        server = Server(
            version=Version.objects.get(version=self._VERSION), name=self._NAME
        )
        server.save()

    def start(self, id: int) -> None:
        pass

    def stop(self) -> None:
        pass
