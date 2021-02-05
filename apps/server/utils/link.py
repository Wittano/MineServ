import asyncio
import threading
from typing import List

from apps.server.models import Version
from .search import search_web
from .version import MINECRAFT_VERSIONS, OFFICIAL, check_latest, search


class SaveThreading(threading.Thread):

    def run(self) -> None:
        from apps.server.models import Version

        result = asyncio.run(_get_links())

        if len(result) > len(Version.objects.all()):
            for v in result:
                if not Version.objects.filter(version=v.version).first():
                    v.save()


async def _get_links() -> List[Version]:
    links = []
    # link from official minecraft website
    official_url = (await search_web(OFFICIAL, "a", {"aria-label": "mincraft version"}))[0][
        "href"
    ]
    latest = await check_latest()

    links.append(Version(version=latest, link=official_url))

    for x in await search_web(MINECRAFT_VERSIONS, "li", {"class": "release"}):
        try:
            href = x.div.find_all("a")[1]["href"]

            if search(x["id"]) and href.startswith("https://launcher.mojang.com/"):
                version = Version(version=x["id"], link=href)
                if version not in links:
                    links.append(version)
        except:
            pass

    return links
