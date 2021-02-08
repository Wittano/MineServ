import os

import aiofiles
import aiohttp

from mineserv.property import DOWNLOAD_DIR

from .search import search_web
from .version import MINECRAFT_VERSIONS, OFFICIAL, check_latest, valid


def exist(directory: str, version: str) -> bool:
    """Check if minecraft server was downloaded previously

    Args:
        directory (str): directory, which will be sought
        version (str): minecraft server version
    """
    return os.path.exists(f"{directory}/minecraft-server-{version}.jar")


class ServerDownloader:
    """Download minecraft server by given version

    Raises:
        ValueError: If minecraft version isn't correct or isn't available
    """

    def __init__(self, version: str):
        self._VERSION = version.lower()

    async def download(self, path: str = None) -> None:
        if exist(DOWNLOAD_DIR if not path else path, self._VERSION):
            return

        if not await valid(self._VERSION):
            raise ValueError("Wrong version format")

        output = "{}/minecraft-server-{}.jar".format(
            DOWNLOAD_DIR if not path else path, self._VERSION
        )

        if self._VERSION == "latest" or self._VERSION == await check_latest():
            url = (await search_web(OFFICIAL, "a", {"aria-label": "mincraft version"}))[
                0
            ]["href"]
        else:
            url = ""
            for u in await search_web(MINECRAFT_VERSIONS, "li", {"class": "release"}):
                if u["id"] == self._VERSION:
                    url = u.div.find_all("a")[1]["href"]
                    break

        async with aiohttp.ClientSession() as session:
            async with session.get(url) as response:
                async with aiofiles.open(
                    output,
                    mode="wb",
                ) as f:
                    await f.write(await response.read())
