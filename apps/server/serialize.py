from rest_framework.serializers import ModelSerializer

from .models import Server


class ServerSerialize(ModelSerializer):
    class Meta:
        model = Server
        fields = ['name', 'version', 'status']
