import os
import re
import threading

import requests
from bs4 import BeautifulSoup
from bs4.element import ResultSet


class ServerDownloader:
    """Download minecrft server by given version

    Raises:
        ValueError: If minecraft version isn't corrent or isn't available
    """

    __REGEX = r"^([\d]{1})\.([\d]{1,2})\.?([\d]?)$"
    __VERSIONS_WEBSITE = "https://www.minecraftversions.com/"
    __OFFICIAL = "https://www.minecraft.net/en-us/download/server"

    class __SaveThread(threading.Thread):
        def __init__(self, output: str, url: str):
            self.__output = output
            self.__url = url
        
        def run(self):
            with requests.get(self.__url) as response:
                with open(
                    self.__output,
                    "wb",
                ) as f:
                    f.write(response.content)

    def __init__(self, version: str):
        self.__LATEST = self.check_latest()

        if self.__search(version) and self.__check_version(version):
            self.__version = version
        elif version.lower() == "latest":
            self.__version = self.__LATEST
        else:
            raise ValueError("Wrong version format")

    def __search(self, version: str) -> bool:
        """Check if minecraft version has correct format

        Args:
            version (str): minecraft version

        Returns:
            bool: True when parameter version is correct with respect to special regex(__REGEX const), otherwise False
        """
        return re.search(self.__REGEX, version) is not None

    def __check_version(self, version: str) -> bool:
        versions = [
            x
            for x in map(
                lambda x: x["id"],
                self.__search_web(self.__VERSIONS_WEBSITE, "li", None),
            )
            if self.__search(x)
        ]

        return version in versions

    def __search_web(self, url: str, tag: str, attrs: dict) -> ResultSet:
        soup = BeautifulSoup(requests.get(url).content, "html.parser")
        return soup.find_all(tag, attrs=attrs)

    def check_latest(self) -> str:
        """
        Check how is the latest minecraft server version
        """
        version = self.__search_web(
            self.__OFFICIAL,
            "a",
            {"aria-label": "mincraft version"},
        )[0].text.split(".")

        return f"{version[1]}.{version[2]}.{version[3]}"

    def download(self):
        output = f"{os.environ['DOWNLOAD_DIR']}/minecraft-server-{self.__version}.jar"

        for _, _, files in os.walk(os.environ["DOWNLOAD_DIR"]):
            for file in files:
                if file.find(f"minecraft-server-{self.__version}.jar") != -1:
                    return

        if self.__version == self.__LATEST:
            url = self.__search_web(
                self.__OFFICIAL,
                "a",
                {"aria-label": "mincraft version"},
            )[0]["href"]
        else:
            url = ""
            for u in self.__search_web(
                self.__VERSIONS_WEBSITE, "li", {"class": "release"}
            ):
                if u["id"] == self.__version:
                    url = u.div.find_all("a")[1]["href"]
                    break

        save = self.__SaveThread(output, url)
        save.run()