import os

import pytest

from mineserv.property import DOWNLOAD_DIR
from .utils.download import ServerDownloader

versions = [
    "1.62.1",
    ";a",
    "1.2.asdfasdfa",
    "asdfgd.2.3",
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
    "1.2.4",
    "latest",
    "LATEST",
    "LateST",
]

versions.extend(correct)


@pytest.mark.parametrize('version', versions)
@pytest.mark.asyncio
@pytest.mark.django_db
async def test_download_sever(version: str):
    try:
        mc = ServerDownloader(version)
        await mc.download()
        with open(
                f"{DOWNLOAD_DIR}/minecraft-server-{version.lower()}.jar",
                "r",
        ) as f:
            assert os.path.getsize(f.name) > 0

    except ValueError:
        assert version not in correct
