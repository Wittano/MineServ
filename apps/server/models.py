from typing import List

from django.db import models


class Version(models.Model):
    version = models.CharField(unique=True, max_length=8)
    link = models.CharField(max_length=500)

    def __gt__(self, other):
        ver1 = self.version.split(".")
        ver2 = (
            other.version.split(".") if isinstance(other, Version) else other.split(".")
        )

        return self._check_gt(ver1, ver2)

    def _check_gt(self, ver1: List[str], ver2: List[str], i: int = 0) -> bool:
        if i < len(ver1):
            return (
                int(ver1[i]) > int(ver2[i])
                if int(ver1[i]) != int(ver2[i])
                else self._check_gt(ver1, ver2, i + 1)
            )
        else:
            return False


class Server(models.Model):
    _SERVER_STATUS = ((1, "stop"), (2, "running"), (3, "online"), (4, "shutdown"))

    name = models.CharField(max_length=100, blank=False, unique=True)
    version = models.ForeignKey(Version, on_delete=models.DO_NOTHING)
    status = models.IntegerField(choices=_SERVER_STATUS, default=1)
    pid = models.IntegerField(null=True, unique=True)
