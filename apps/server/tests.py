import os
import re

from django.test import TestCase

from .download import MinecraftDownloader, regex

# Create your tests here.


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
            # "1.16.4",
            "latest",
            "LATEST",
            "LateST",
        ]

        versions.extend(correct)

        for version in versions:
            try:
                mc = MinecraftDownloader(version)
                mc.download()

                with open(
                    "{}/minecraft-server-{}.jar".format(
                        os.environ["DOWNLOAD_DIR"],
                        version if version.lower() != "latest" else mc._version,
                    ),
                    "r",
                ) as f:
                    assert os.path.getsize(f.name) > 0
            except ValueError:
                print(version)
                assert version not in correct
