from django.contrib import admin
from django.urls import path
from userprofile import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns=[
    path('',views.profilep,name='profilep'),
    path('update_profile',views.update_profile,name='update_profile'),
    path('Addbio',views.Addbio,name='Addbio'),
    path('Predicthp',views.Predicthp,name='Predicthp'),
     path('familypred',views.familypred,name='familypred'),
    path('crushpred',views.crushpred,name='crushpred'),
    path('relatpred',views.relatpred,name='relatpred'),
    path('friendpred',views.friendpred,name='friendpred'),


]+ static(settings.MEDIA_URL,document_root=settings.MEDIA_ROOT)