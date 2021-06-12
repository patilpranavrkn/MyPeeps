from django.shortcuts import render,HttpResponse,redirect
from django.contrib.auth.models import User
import login.views
from django.contrib.auth import authenticate, login,logout
from django.contrib import messages
from userprofile.models import *
import os

# Create your views here.


def home(request):
    return render(request,'login/login.html')


def my_login(request):
    if request.method=="POST":
        username=request.POST.get('username','')
        password=request.POST.get('password','')
        print("88888888888887777777777--->",os.getcwd())

        print(username,password)
        user=authenticate(username=username,password=password)
        print(user.email+"   ===  ",user.username)
        print(user)
        if user is not None:
            login(request,user)
            print("*****************************")
            print((Profile.objects.all().values()))
            
            print("*****************************")
           # messages.success(request,"Logged in")
            
            return redirect('/profile')

        else:
            messages.error(request,"Invalid Credentials")
            return redirect('/') 

def App_logout(request):
    logout(request)
    return redirect('homepage')
          
def signup(request):
    if request.method=="POST":
        mail =request.POST.get('email','')
        name =request.POST.get('name','') 
        username=request.POST.get('username','') 
        password=request.POST.get('password','') 
        confirmpassword =request.POST.get('confpassword','')   

        userCheck=User.objects.filter(username=username) 


        if userCheck:
            messages.error(request,"Username already exits") 
            return redirect('/')

        print(mail,name)

        if(password==confirmpassword):
            user_obj=User.objects.create_user(first_name=name,username=username,password=password,email=mail)
            user_obj.save()
            
            profile_obj=Profile()
            profile_obj.user=user_obj
            profile_obj.save()
            
            return redirect('/')

def signupr(request):
    return render(request,'login/Signup.html')
    