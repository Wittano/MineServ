from django.urls import re_path
from . import views

urlpatterns = [
    re_path(r'(?P<id>[0-9]+)', views.properties),
]
