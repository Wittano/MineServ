from typing import List, Tuple, Union

from jproperties import Properties

from apps.server.models import Version


def _property(path: str, settings: Union[Tuple[str], List[Tuple[str]]]) -> None:
    """Set new settings for .properties files """
    p = Properties()

    with open(path, "rb") as f:
        p.load(f, encoding="utf-8")

    with open(path, "wb") as f:
        if isinstance(settings, Tuple):
            p[settings[0]] = settings[1]
        else:
            for s in settings:
                p[s[0]] = s[1]

        p.store(f, "utf-8")


def _eula(path: str):
    """Change eula settings from false to true in eula.txt"""
    _property(path, ("eula", "true"))


def template(path: str, version: str, ip: str = "127.0.0.1") -> None:
    """Create base template for server

    Args:
        path (str): path to server directory
        version (str): minecraft server version
        ip (str): IP minecraft server
    """
    if Version(version=version) > "1.7.9":
        _eula(f"{path}/eula.txt")

    _property(f"{path}/server.properties", ("server-ip", ip))
