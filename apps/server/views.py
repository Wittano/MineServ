from rest_framework.decorators import (
    api_view,
    renderer_classes,
    permission_classes,
    authentication_classes,
)
from rest_framework.generics import ListAPIView
from rest_framework.permissions import IsAuthenticated
from rest_framework.renderers import JSONRenderer, TemplateHTMLRenderer
from rest_framework.request import HttpRequest
from rest_framework.response import Response
from rest_framework_simplejwt.authentication import JWTAuthentication

from .models import Version, Server
from .serialize import ServerSerialize, VersionSerialize
from .utils.java.server import MinecraftServer


class VersionListView(ListAPIView):
    queryset = Version.objects.all()
    serializer_class = VersionSerialize


class ServerListView(ListAPIView):
    queryset = Server.objects.all()
    serializer_class = ServerSerialize

    def list(self, request, *args, **kwargs):
        data = self.queryset.filter(user_id=request.user.id)
        serialize = self.serializer_class(data, many=True)

        return Response(serialize.data)


@api_view(["POST"])
def create(request: HttpRequest) -> Response:
    try:
        mc = MinecraftServer(request.data["name"], request.data["version"])
        mc.create(request.user)
    except (ValueError, Version.DoesNotExist):
        return Response(status=400, data={"message": "Wrong version format"})

    server = ServerSerialize(Server.objects.get(name=request.data["name"]))

    return Response(status=200, data=server.data)


@api_view(["POST"])
def start(request: HttpRequest, id: int) -> Response:
    try:
        server = Server.objects.get(id=id)
        mc = MinecraftServer(server.name, server.version.version)
        mc.start(id)

        serialize = ServerSerialize(server)
    except (ValueError, Version.DoesNotExist, Server.DoesNotExist):
        return Response(status=400, data={"message": "Wrong version format"})
    except Server.DoesNotExist:
        return Response(status=400, data={"message": "Server doesn't exist"})

    return Response(status=200, data=serialize.data)


@api_view(["POST"])
def stop(request: HttpRequest, id: int) -> Response:
    try:
        MinecraftServer.stop(id)

        server = ServerSerialize(Server.objects.get(id=id))
    except Server.DoesNotExist:
        return Response(status=400, data={"message": "Server doesn't exist"})

    return Response(status=200, data=server.data)


@api_view(["DELETE"])
def delete(request: HttpRequest, id: int) -> Response:
    try:
        server = Server.objects.get(id=id)
        mc = MinecraftServer(server.name, server.version.version)
        mc.delete(id)
    except (ValueError, Version.DoesNotExist):
        return Response(status=400, data={"message": "Wrong version format"})

    return Response(status=200)
