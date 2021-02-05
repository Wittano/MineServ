from django.apps import AppConfig


class ServerConfig(AppConfig):
    name = "apps.server"

    def ready(self):
        from apps.server.utils.link import SaveThreading

        SaveThreading().start()
