from django.test import TestCase

from .models import Server
from .utils.link import SaveThreading


class ServerTest(TestCase):
    def _create(self):
        SaveThreading().run()

        return self.client.post(
            "/server/create", data={"name": "test-1.16.5", "version": "1.16.5"}
        )

    def test_create_server(self):
        response = self._create()

        assert response.status_code == 200
        assert Server.objects.count() == 1

    def test_start_server(self):
        self._create()
        response = self.client.post("/server/start/1")

        server = Server.objects.get(id=1)

        assert response.status_code == 200
        assert server.pid is not None
        assert server.status == 2

        self.client.post("/server/stop/1")

    def test_stop_server(self):
        self._create()
        self.client.post("/server/start/1")
        response = self.client.post(
            "/server/stop/1",
        )
        server = Server.objects.get(id=1)

        assert response.status_code == 200
        assert server.pid is None
        assert server.status == 1

    def test_delete_server(self):
        self._create()
        response = self.client.delete("/server/delete/1")

        assert response.status_code == 200
        assert Server.objects.count() == 0
