from django.shortcuts import render
from django.shortcuts import render,HttpResponse,redirect
from userprofile.models import *
# Create your views here.


def profilep(request):

    print("inside the profile page")

    profile_obj=Profile.objects.filter(user=request.user)
    # profile_obj=None
    print(profile_obj)
    data_key={}

    data_key["profile_info"]=profile_obj

    
  
   
    return  render(request,'user/userProfile.html',data_key)

def update_profile(request):
    profile_obj=Profile.objects.filter(user=request.user)
    # profile_obj=None
    print(profile_obj)
    data_key={}
    print("here we are")
    data_key["profile_info"]=profile_obj

    
    
    return render(request,'user/update_profile.html',data_key)

def Addbio(request):

    bio=request.POST.get('bio','')
    Profile.objects.filter(user=request.user).update(bio=bio)

    return HttpResponse("Updated")

