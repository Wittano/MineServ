from django.db import models


class Version(models.Model):
    version = models.CharField(unique=True, max_length=8)
    link = models.CharField(max_length=500)


class Server(models.Model):
    _SERVER_STATUS = ((1, "stop"), (2, "running"), (3, "online"), (4, "shutdown"))

    name = models.CharField(max_length=100, blank=False, unique=True)
    version = models.ForeignKey(Version, on_delete=models.DO_NOTHING)
    status = models.IntegerField(choices=_SERVER_STATUS, default=1)
    pid = models.IntegerField(null=True, unique=True)
