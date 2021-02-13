from rest_framework.decorators import api_view, renderer_classes, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.renderers import JSONRenderer, TemplateHTMLRenderer
from rest_framework.request import HttpRequest
from rest_framework.response import Response
from rest_framework_simplejwt.authentication import JWTAuthentication

from .models import Version, Server
from .utils.java.server import MinecraftServer


@api_view(["POST"])
@permission_classes([IsAuthenticated])
@authentication_classes([JWTAuthentication])
@renderer_classes((JSONRenderer, TemplateHTMLRenderer))
def create(request: HttpRequest) -> Response:
    try:
        mc = MinecraftServer(str(request.POST["name"]), str(request.POST["version"]))
        mc.create()
    except (ValueError, Version.DoesNotExist):
        return Response(status=400, data={"message", "Wrong version format"})

    return Response(status=200)


@api_view(["POST"])
@permission_classes([IsAuthenticated])
@authentication_classes([JWTAuthentication])
@renderer_classes((JSONRenderer, TemplateHTMLRenderer))
def start(request: HttpRequest, id: int) -> Response:
    try:
        server = Server.objects.get(id=id)
        mc = MinecraftServer(server.name, server.version.version)
        mc.start(id)
    except (ValueError, Version.DoesNotExist, Server.DoesNotExist):
        return Response(status=400, data={"message", "Wrong version format"})
    except Server.DoesNotExist:
        return Response(status=400, data={"message", "Server doesn't exist"})

    return Response(status=200)


@api_view(["POST"])
@permission_classes([IsAuthenticated])
@authentication_classes([JWTAuthentication])
@renderer_classes((JSONRenderer, TemplateHTMLRenderer))
def stop(request: HttpRequest, id: int) -> Response:
    try:
        MinecraftServer.stop(id)
    except Server.DoesNotExist:
        return Response(status=400, data={"message", "Server doesn't exist"})

    return Response(status=200)


@api_view(["DELETE"])
@permission_classes([IsAuthenticated])
@authentication_classes([JWTAuthentication])
@renderer_classes((JSONRenderer, TemplateHTMLRenderer))
def delete(request: HttpRequest, id: int) -> Response:
    try:
        server = Server.objects.get(id=id)
        mc = MinecraftServer(server.name, server.version.version)
        mc.delete(id)
    except (ValueError, Version.DoesNotExist):
        return Response(status=400, data={"message", "Wrong version format"})

    return Response(status=200)
