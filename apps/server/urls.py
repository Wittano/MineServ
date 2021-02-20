from django.urls import path

from . import views

urlpatterns = [
    path(r"", views.ServerListView.as_view()),
    path(r"version", views.VersionListView.as_view()),
    path(r"create", views.create),
    path(r"start/<int:id>", views.start),
    path(r"stop/<int:id>", views.stop),
    path(r"delete/<int:id>", views.delete),
]
