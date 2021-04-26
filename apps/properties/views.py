from django.shortcuts import render
from rest_framework.request import Request
from rest_framework.response import Response
from rest_framework.decorators import api_view
from apps.server.models import Server
from mineserv.property import DOWNLOAD_DIR
from jproperties import Properties


def _load_properties(name: str) -> Properties:
    """Load settings from server.properties

    Args:
        name (str): server name

    Returns:
        Properties: jproperties object, which contain all of the settings from server.properties
    """
    p = Properties()

    with open(f'{DOWNLOAD_DIR}/{name}/server.properties', "rb") as f:
        p.load(f)

    return p


@api_view(["GET", "PUT"])
def properties(request: Request, id: int) -> Response:
    server = Server.objects.get(id=id)

    if request.method == 'PUT':
        p = _load_properties(server.name)

        p.update(request.data)

        with open(f'{DOWNLOAD_DIR}/{server.name}/server.properties', "wb") as f:
            p.store(f, encoding='utf-8')

        return Response(status=204)
    else:
        p = _load_properties(server.name)
        return Response(status=200, data=p.properties)
