import re

from .search import search_web

REGEX = r"^([\d]{1})\.([\d]{1,2})\.?([\d]?)$"
MINECRAFT_VERSIONS = "https://www.minecraftversions.com/"
OFFICIAL = "https://www.minecraft.net/en-us/download/server"


async def valid(version: str) -> bool:
    return (
                   search(version) and await check_version(version)
           ) or version.lower() == "latest"


async def check_latest() -> str:
    """Check how is the latest minecraft server version

    Returns:
        str: The latest stable version of minecraft
    """
    version = (await search_web(OFFICIAL, "a", {"aria-label": "mincraft version"}))[
        0
    ].text.split(".")

    return f"{version[1]}.{version[2]}.{version[3]}"


def search(version: str) -> bool:
    """Check if minecraft version has correct format

    Args:
        version (str): minecraft version

    Returns:
        bool: True when parameter version is correct with respect to special regex(__REGEX const), otherwise False
    """
    return re.search(REGEX, version) is not None


async def check_version(version: str) -> bool:
    """Check if version of minecraft server is available to download

    Args:
        version (str): Minecraft version

    Returns:
        bool: True when method found correct version, otherwise False
    """
    versions = [
        x
        for x in map(
            lambda x: x["id"],
            await search_web(MINECRAFT_VERSIONS, "li"),
        )
        if search(x)
    ]
    versions.extend([await check_latest()])

    return version in versions
