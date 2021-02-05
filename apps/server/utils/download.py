import glob

import aiofiles
import aiohttp

from mineserv.property import DOWNLOAD_DIR
from .search import search_web
from .version import MINECRAFT_VERSIONS, OFFICIAL, valid


class ServerDownloader:
    """Download minecraft server by given version

    Raises:
        ValueError: If minecraft version isn't correct or isn't available
    """

    def __init__(self, version: str):
        self._VERSION = version

    async def download(self):
        if not await valid(self._VERSION):
            raise ValueError("Wrong version format")

        output = f"{DOWNLOAD_DIR}/minecraft-server-{self._VERSION.lower()}.jar"

        if glob.glob(output):
            return

        if self._VERSION == "latest":
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
