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

    print("ewwww**********")
    print("inside add bio")

    if(len(request.FILES)==1):
        print(request.FILES)
        image=request.FILES['img']    
        bio=request.POST.get('bio','')
        Profile.objects.filter(user=request.user).update(bio=bio)
        Profile.objects.filter(user=request.user).update(userImage=image)

        prop_obj=Profile.objects.get(request.user)
        


    else:
        bio=request.POST.get('bio','')
        Profile.objects.filter(user=request.user).update(bio=bio)
        

    return HttpResponse("Updated")

def Predicthp(request):


    return render(request,'user/Predicthp.html')

def familypred(request):

    

    return render(request,"user/familypred.html")    

def crushpred(request):

    return render(request,"user/crushpred.html")  

def relatpred(request):

    return render(request,"user/relatpred.html")  

def friendpred(request):

    return render(request,"user/friendpred.html")      