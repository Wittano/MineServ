from django.urls import path
from rest_framework_simplejwt import views as jwt

from . import views

urlpatterns = [
    path(r'user', views.UserCreateView.as_view(), name='register'),
    path(r'token', jwt.TokenObtainPairView.as_view(), name='login'),
    path(r'token/refresh', jwt.TokenRefreshView.as_view(), name='refresh'),
]
