from typing import Dict

from rest_framework.test import APITestCase

from .models import Server
from .utils.link import SaveThreading


class ServerTest(APITestCase):
    def _auth(self):
        user = {
            "username": "tester",
            "password": "12345"
        }
        self.client.post("/api/user", data=user, follow=True)
        response = self.client.post("/api/token", data=user, follow=True)
        self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {response.data["access"]}')

        return {"Authorization": f'Bearer {response.data["access"]}'}

    def _create(self, header: Dict):
        SaveThreading().run()

        return self.client.post(
            "/server/create",
            data={"name": "test-1.16.5", "version": "1.16.5"},
            follow=True,
            **header,
        )

    def test_create_server(self):
        header = self._auth()

        response = self._create(header)

        assert response.status_code == 200
        assert Server.objects.count() == 1

    def test_start_server(self):
        header = self._auth()

        self._create(header)

        response = self.client.post("/server/start/1", follow=True, **header)

        server = Server.objects.get(id=1)

        assert response.status_code == 200
        assert server.pid is not None
        assert server.status == 2

        self.client.post("/server/stop/1", follow=True, **header)

    def test_stop_server(self):
        header = self._auth()

        self._create(header)
        self.client.post("/server/start/1", follow=True, **header)
        response = self.client.post("/server/stop/1", follow=True, **header)
        server = Server.objects.get(id=1)

        assert response.status_code == 200
        assert server.pid is None
        assert server.status == 1

    def test_delete_server(self):
        header = self._auth()

        self._create(header)
        response = self.client.delete("/server/delete/1", follow=True, **header)

        assert response.status_code == 200
        assert Server.objects.count() == 0
