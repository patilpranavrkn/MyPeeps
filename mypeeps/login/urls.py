from django.contrib import admin
from django.urls import path
from login import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns=[
    path('',views.home,name='homepage'),
    path('login',views.my_login,name='my_login'),
    path('signup',views.signup,name='signup'),
    path('logout',views.App_logout,name='logout'),
    path('loginl',views.home,name='homepage'),
    path('signupl',views.signupr,name='signupr'),
   
]