from django.apps import AppConfig


class ServerConfig(AppConfig):
    name = "apps.server"

    def ready(self):
        from .utils.link import SaveThreading

        SaveThreading().start()
