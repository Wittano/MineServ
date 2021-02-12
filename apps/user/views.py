from django.contrib.auth.models import User
from rest_framework.generics import CreateAPIView

from .serialize import UserSerializer


class UserCreateView(CreateAPIView):
    model = User
    serializer_class = UserSerializer
