import os

from django.test import TestCase

from apps.general.observer import Observer

from .download import ServerDownloader


class DownloadObserver(Observer):
    def __init__(self, version, mc):
        self.__version = version
        self.__mc = mc

    def update(self):
        with open(
            "{}/minecraft-server-{}.jar".format(
                os.environ["DOWNLOAD_DIR"],
                self.__version
                if self.__version.lower() != "latest"
                else self.__mc.check_latest(),
            ),
            "r",
        ) as f:
            assert os.path.getsize(f.name) > 0


class ServerTests(TestCase):
    def test_download_sever(self):
        versions = [
            "1.62.1",
            ";a",
            "1.2.asdfasdfa",
            "asdfgd.2.3",
            "1.2.3",
            "1.asdf.2",
            "3.5.6",
            "1.17.1",
            "7.6.6",
            " ",
            "\n",
            "dirt",
        ]

        correct = [
            "1.9",
            "latest",
            "LATEST",
            "LateST",
        ]

        versions.extend(correct)

        for version in versions:
            try:
                mc = ServerDownloader(version)
                mc.observer = DownloadObserver(version, mc)
                mc.download()
            except ValueError:
                assert version not in correct
