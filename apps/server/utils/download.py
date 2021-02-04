import glob

import aiofiles
import aiohttp
from mineserv.property import DOWNLOAD_DIR

from .search import search_web
from .version import MINECRAFT_VERSIONS, OFFICIAL, check_latest, valid


class ServerDownloader:
    """Download minecrft server by given version

    Raises:
        ValueError: If minecraft version isn't corrent or isn't available
    """

    def __init__(self, version: str):
        self.__VERSION = version

    async def download(self):
        if not await valid(self.__VERSION):
            raise ValueError("Wrong version format")

        output = f"{DOWNLOAD_DIR}/minecraft-server-{self.__VERSION.lower()}.jar"

        if glob.glob(output) != []:
            return

        if self.__VERSION == "latest":
            url = await search_web(OFFICIAL, "a", {"aria-label": "mincraft version"})
            url = url[0]["href"]
        else:
            url = ""
            for u in await search_web(MINECRAFT_VERSIONS, "li", {"class": "release"}):
                if u["id"] == self.__VERSION:
                    url = u.div.find_all("a")[1]["href"]
                    break

        async with aiohttp.ClientSession() as session:
            async with session.get(url) as response:
                async with aiofiles.open(
                    output,
                    mode="wb",
                ) as f:
                    await f.write(await response.read())
