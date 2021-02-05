from apps.server.models import Server

from .download import ServerDownloader


class MinecraftServer:
    def __init__(self, version: str):
        self._VERSION = version
        self._MC = ServerDownloader(self._VERSION)

    async def create(self, name: str, version: str = "latest") -> None:
        server = Server(version, name)
        server.save()

        pass

    def start(self, id: int) -> None:
        pass

    def stop(self) -> None:
        pass
