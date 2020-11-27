from django.contrib import admin
from django.urls import path
from userprofile import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns=[
    path('',views.profilep,name='profilep'),
    path('update_profile',views.update_profile,name='update_profile'),

]