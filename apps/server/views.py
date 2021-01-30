import os

from django.shortcuts import render
from django.http import HttpRequest, HttpResponse


# Create your views here.
def run(request: HttpRequest):
    return HttpResponse(os.environ['DB_PASSWD']) 