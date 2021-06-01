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
    path('selfpred',views.selfpred,name='selfpred'),
    path('familypred',views.familypred,name='familypred'),
    path('crushpred',views.crushpred,name='crushpred'),
    path('relatpred',views.relatpred,name='relatpred'),
    path('friendpred',views.friendpred,name='friendpred'),
    path('Procselfpred',views.Procselfpred,name='Procselfpred'),
    path('Procfamilypred',views.Procfamilypred,name='Procfamilypred'),
    path('Proccrushpred',views.Proccrushpred,name='Proccrushpred'),
    path('Procrelatpred',views.Procrelatpred,name='Procrelatpred'),
    path('Procfriendpred',views.Procfriendpred,name='Procfriendpred'),
    path('Buddyhp',views.Buddyhp,name='Buddyhp'),
    path('selfpredbud',views.selfpredbud,name='selfpredbud'),
    path('familypredbud',views.familypredbud,name='familypredbud'),
    path('crushpredbud',views.crushpredbud,name='crushpredbud'),
    path('relatpredbud',views.relatpredbud,name='relatpredbud'),
    path('friendpredbud',views.friendpredbud,name='friendpredbud'),
    path('checkgibber',views.checkGibberandGiveResponse,name='checkGibber'),
    path('familypredjson',views.familypred_json,name='familypred_json'),
    path('relatpredjson',views.relationpred_json,name='relatpred_json'),
    path('crushpredjson',views.crushpred_json,name='crushpred_json'),
    path('selfpredjson',views.selfpred_json,name='selfpred_json'),
    path('friendpredjson',views.friendpred_json,name='friendpred_json'),
    path('save_reaction',views.save_reaction,name='save_reaction_json'),
    path('get_previous_predictions',views.Get_Previous_predictions,name='get_previous_predictions'),
    path('open_chat_bot',views.Open_Chat_Bot,name='open_chat_bot'),
    path('logout',views.App_logout,name='logout'),
    path('share_feedback',views.share_feedback,name='share_feedback'),
    path('save_feedback',views.save_feedback,name='save_feedback'),
    path('update_reaction',views.update_reaction,name='update_reaction_json'),

]+ static(settings.MEDIA_URL,document_root=settings.MEDIA_ROOT)