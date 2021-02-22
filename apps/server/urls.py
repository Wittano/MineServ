from django.urls import path
from django.conf.urls import url

from . import views

urlpatterns = [
    path(r"", views.ServerListView.as_view()),
    url(r"^version$", views.VersionListView.as_view()),
    url(r"^version/(?P<id>[0-9]+)$", views.VersionListView.as_view()),
    path(r"create", views.create),
    path(r"start/<int:id>", views.start),
    path(r"stop/<int:id>", views.stop),
    path(r"delete/<int:id>", views.delete),
]
