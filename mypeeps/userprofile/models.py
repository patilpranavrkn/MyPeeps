from django.db import models
from django.contrib.auth.models import User


# Create your models here.


class Profile(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    userImage=models.ImageField(upload_to="Profiles",blank=True)
    bio=models.CharField(max_length=100,blank=True)
    friends=models.IntegerField(default=0)
    tribe=models.IntegerField(default=0)
    

    


class Post(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    topic=models.CharField(max_length=100)
    caption=models.CharField(max_length=200)
    date=models.DateTimeField(auto_now_add=True, blank=True)
    image=models.ImageField(upload_to="user_image",blank=True)


    def __str__(self):
        return str(self.user) +" "+str(self.date.date())