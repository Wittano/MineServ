from django.urls import path

from . import views

urlpatterns = [
    path(r"create", views.create),
    path(r"start/<int:id>", views.start),
    path(r"stop/<int:id>", views.stop),
    path(r"delete/<int:id>", views.delete),
]
