from django.db import models

# Create your models here.


class Version(models.Model):
    version = models.CharField(unique=True, max_length=8)
    link = models.CharField(unique=True, max_length=500)
