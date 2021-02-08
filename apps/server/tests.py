import os

import pytest
from jproperties import Properties

from mineserv.property import DOWNLOAD_DIR

from .models import Version
from .utils.download import ServerDownloader, exist
from .utils.link import SaveThreading
from .utils.properites import _property
from .utils.server import MinecraftServer

versions = [
    "1.62.1",
    ";a",
    "1.2.asdfasdfa",
    "asdfgd.2.3",
    "1.asdf.2",
    "1.17.1",
    "7.6.6",
    " ",
    "\n",
]

correct = [
    "1.9",
    "1.16.5",
    "1.2.4",
    "latest",
    "LateST",
]

versions.extend(correct)


@pytest.mark.parametrize("version", versions)
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


@pytest.mark.parametrize("property", [("a", "b"), [("a", "b"), ("b", "d")]])
def test_properties_property(property):
    path = "/tmp/test.properties"

    if not os.path.exists(path):
        with open(path, "w") as f:
            f.write("s=s")

    _property(path, property)

    with open(path, "rb") as f:
        p = Properties()
        p.load(f, "utf-8")

        if isinstance(property, tuple):
            assert p[property[0]].data == property[1]
        else:
            assert p[property[0][0]].data == property[0][1]


@pytest.mark.parametrize("version", ["1.16.5", "1.4.4", "6.6.6", ""])
@pytest.mark.django_db
def test_server_create(version: str):
    SaveThreading().run()

    try:
        server = MinecraftServer(f"test-{version}", version)
        server.create()
    except Version.DoesNotExist:
        assert True
        return
    except ValueError:
        assert True
        return
    else:
        pass

    path = f"{DOWNLOAD_DIR}/test-{version}"

    assert exist(path, version)

    p = Properties()

    if Version(version=version) > "1.7.9":
        with open(f"{path}/eula.txt", "rb") as f:
            p.load(f, "utf-8")
            assert p["eula"].data == "true"

    with open(f"{path}/server.properties", "rb") as f:
        p.load(f, "utf-8")
        assert p["server-ip"].data == "127.0.0.1"


@pytest.mark.parametrize("version", ["1.4.4", "1.5.6", "1.16.4", "1.7.10"])
def test_version_gt(version: str):
    ver1 = Version(version="1.4.3")
    ver2 = Version(version=version)

    assert ver2 > ver1
