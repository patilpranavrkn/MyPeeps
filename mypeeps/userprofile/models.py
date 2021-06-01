from django.db import models
from django.contrib.auth.models import User


# Create your models here.



class Predictions(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    Pred_id=models.AutoField(primary_key=True)
    Pred_type=models.CharField(max_length=20,blank=True)
    Pred_name=models.CharField(max_length=20,blank=True)
    Pred_input_str=models.CharField(max_length=100,blank=True)
    Pred_number=models.IntegerField()
    Pred_reaction=models.CharField(max_length=10,blank=True,default="NEUTRAL")
    Person_name=models.CharField(max_length=30,blank=True)
    Date_today=models.DateField(auto_now=True)
    



class Profile(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    userImage=models.ImageField(upload_to="Profiles",blank=True)
    bio=models.CharField(max_length=100,blank=True)
    friends=models.IntegerField(default=0)
    tribe=models.IntegerField(default=0)
    
class CBQAStored(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    Ques_id=models.AutoField(primary_key=True)
    Quest_text=models.TextField()
    Answer_text=models.TextField()
    
class Feedback(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    Feedback_id=models.AutoField(primary_key=True)
    Quest_make_sense=models.CharField(max_length=100,blank=True)
    Reaction_thoughts=models.CharField(max_length=100,blank=True)
    Advice=models.TextField()
    
    

    
'''

class Post(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    topic=models.CharField(max_length=100)
    caption=models.CharField(max_length=200)
    date=models.DateTimeField(auto_now_add=True, blank=True)
    image=models.ImageField(upload_to="Profile/%Y/%m/%d",blank=True)


    def __str__(self):
        return str(self.user) +" "+str(self.date.date())

'''        