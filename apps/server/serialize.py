from rest_framework.serializers import ModelSerializer

from .models import Server, Version


class ServerSerialize(ModelSerializer):
    class Meta:
        model = Server
        fields = ["id", "name", "version", "status"]


class VersionSerialize(ModelSerializer):
    class Meta:
        model = Version
        fields = "__all__"
