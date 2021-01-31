import os
import re

import requests
from bs4 import BeautifulSoup

regex = r"^([\d]{1})\.([\d]{1,2})\.?([\d]?)$"


class MinecraftDownloader:
    def __init__(self, version: str):
        self._latest = self._check_latest()
        self._dir = os.environ["DOWNLOAD_DIR"]

        if self._search(version) and self._check_version(version):
            self._version = version
        elif version.lower() == "latest":
            self._version = self._latest
        else:
            raise ValueError("Wrong version format")

    def download(self):
        output = f"{self._dir}/minecraft-server-{self._version}.jar"

        for _, _, files in os.walk(self._dir):
            for file in files:
                if file.find(f"minecraft-server-{self._version}.jar") != -1:
                    return

        soup = BeautifulSoup(
            requests.get("https://www.minecraftversions.com/").content, "html.parser"
        )

        url = ""

        if self._version.lower() == self._latest:
            official = BeautifulSoup(
                requests.get("https://www.minecraft.net/en-us/download/server").content,
                "html.parser",
            )
            url = official.find("a", attrs={"aria-label": "mincraft version"})["href"]
        else:
            for u in soup.select(".release"):
                if u["id"] == self._version:
                    url = u.div.find_all("a")[1]["href"]
                    break

        with requests.get(url) as response:
            with open(
                output,
                "wb",
            ) as f:
                f.write(response.content)

    def _search(self, version: str) -> bool:
        return re.search(regex, version) is not None

    def _check_version(self, version: str) -> bool:
        soup = BeautifulSoup(
            requests.get("https://www.minecraftversions.com/").content, "html.parser"
        )
        versions = [
            x
            for x in map(lambda x: x["id"], soup.select(".list-group-item,.release"))
            if self._search(x)
        ]

        return version in versions

    def _check_latest(self) -> str:
        """
        Check how is the latest minecraft server version
        """
        soup = BeautifulSoup(
            requests.get("https://www.minecraft.net/en-us/download/server").content,
            "html.parser",
        )
        version = soup.find("a", {"aria-label": "mincraft version"}).text.split(".")

        return f"{version[1]}.{version[2]}.{version[3]}"
