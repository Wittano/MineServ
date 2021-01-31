import os
import re

from django.test import TestCase

from .download import ServerDownloader

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
            "latest",
            "LATEST",
            "LateST",
        ]

        versions.extend(correct)

        for version in versions:
            try:
                mc = ServerDownloader(version)
                mc.download()

                with open(
                    "{}/minecraft-server-{}.jar".format(
                        os.environ["DOWNLOAD_DIR"],
                        version if version.lower() != "latest" else mc.check_latest(),
                    ),
                    "r",
                ) as f:
                    assert os.path.getsize(f.name) > 0
            except ValueError:
                assert version not in correct
