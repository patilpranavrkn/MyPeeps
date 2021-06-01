from django.shortcuts import render
from django.shortcuts import render,HttpResponse,redirect
from userprofile.models import *
import numpy as np
from tensorflow.keras.models import load_model
import pandas as pd
import os
from userprofile.models import *
from modules.processreply import ProcessReply
from modules.gibber_classify import classify
from django.http import JsonResponse
#from modules.checkcb import getReplyForInputQuest
#from modules.chatbot_service import  Predict_from_cb
# Create your views here.

#new1

#from tensorflow.keras.models import load_model
import pickle
import random
import nltk
nltk.download('punkt')
nltk.download('wordnet')
from nltk.stem import WordNetLemmatizer
#import numpy as np

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense,Activation,Dropout
from tensorflow.keras.optimizers import SGD
model=None
intents=None
lemmatizer=None
words=None
classes=None
def initialize():
    global model
    global intents
    global lemmatizer
    global words
    global classes
    words=pickle.load(open('words.pk1','rb'))
    classes=pickle.load(open('classes.pk1','rb'))
    lemmatizer=WordNetLemmatizer()
    model=load_model('chatbot_modecrush.h5')
    intents=pickle.load(open('sample_int_crush.p','rb'))

def clean_up_sentence(sentence):
    # tokenizing the pattern
    sentence_words = nltk.word_tokenize(sentence)
    # stemming each word
    sentence_words = [lemmatizer.lemmatize(word) for word in sentence_words]
    return sentence_words

def bag_of_words(sentence):
  sentence_words=clean_up_sentence(sentence)
  bag=[0]*len(words)
  for w in sentence_words:
    for i,word in enumerate(words):
      if word==w:
        bag[i]=1

  return np.array(bag)
def predict_class(sentence):
  bow=bag_of_words(sentence)
  
  res=model.predict(np.array([bow]))[0]
  
  ERROR_THRESHOLD=0.25
  results=[[i,r] for i,r in enumerate(res) if r> ERROR_THRESHOLD]

  results.sort(key=lambda x:x[1],reverse=True)
  return_list=[]
  
  for r in results:
    return_list.append({'intent':classes[r[0]],'probability':str(r[1])})
    
    return return_list
def get_response(intents_list,intents_json):
  print(intents_list,intents_json)
  tag=intents_list[0]['intent']
  list_of_intents=intents_json['intents']
  for i in list_of_intents:
    if i['tag']==tag:
      result=random.choice(i['responses'])
      break
  return result       
#initialize()
#ints=predict_class('This is it?')

#res=get_response(ints,intents)

def getReplyForInputQuest(inputquest):
    initialize()
    ints=predict_class(inputquest)
    res=get_response(ints,intents)
    return res
#    


def profilep(request):



    profile_obj=Profile.objects.filter(user=request.user)
    # profile_obj=None
    #print(profile_obj)
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

    request.session["pred_type"]=None
    #return render(request,'user/Predicthp.html')
    return render(request,"user/menu_rn_a.html")
'''
def selfpred(request):
    request.session["pred_type"]="self"
    return render(request,'user/selfpred.html')    
'''
def familypred(request):
    request.session["pred_type"]="family"
    return render(request,"user/familypred.html")    

def crushpred(request):
    request.session["pred_type"]="crush"
    return render(request,"user/crushpred.html")  

def relatpred(request):
    request.session["pred_type"]="relat"
    return render(request,"user/relatpred.html")  

def friendpred(request):
    request.session["pred_type"]="friend"
    return render(request,"user/friendpred.html") 
   

def get_Model(mname):

    export_path = os.path.join(os.getcwd(),"Models", mname+".h5")
    loaded_model =load_model(export_path)

    return loaded_model
    

def TransformData(dlist):
    nparray=np.array(dlist)
    pdf=pd.DataFrame([nparray])

    return pdf

def get_Predictions(Model,nparray):

    pred=Model.predict_classes(nparray)
    index=0
    print(type(pred))
    print(pred[0])
    print(type(pred[0]),"ewww")
    tpred=pred[0]
    return tpred    # returning the class of the predictions    
    
'''              
def Procselfpred(request):
    
    opinion=[]
    
    for i in range (1,9):
        print(opinion)
        print()
        opinion.append(int(request.POST.get('opt'+str(i))))
    npop= np.array(opinion)
    model=get_Model("model_self")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_self",predictions)  
    Attributes,Attributesnot,AttributesCurr=ReadlinesFromInfoFile(fname,"self_dir")
    
    return render(request,"user/Self_Report.html",{"A1":Attributes,"A2":Attributesnot,"A3":AttributesCurr,"rel_type_text":fname}) 
    return HttpResponse("Done")
def Procfamilypred(request):
    opinion=[]
    person_name=request.POST.get('pername')
    for i in range (1,5):
        opinion.append(int(request.POST.get('opt'+str(i))))


    npop= np.array(opinion)
    model=get_Model("model_family")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_fd",predictions)  
    Attributes,Attributesnot,AttributesCurr=ReadlinesFromInfoFile(fname,"fd_dir")
    return HttpResponse("Done")
    return render(request,"user/Fam_Report.html",{"A1":Attributes,"A2":Attributesnot,"A3":AttributesCurr,"rel_type_text":fname}) 
def Proccrushpred(request):
    opinion=[]
    print(request.POST.get('frdname'),"++++++++++++++++++")
    person_name=request.POST.get('pername')
    for i in range (1,6):
        opinion.append(int(request.POST.get('opt'+str(i))))

    print(opinion,"The opinion is")    

    npop= np.array(opinion)
    model=get_Model("model_crush")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_crush",predictions)  
    Attributes,Attributesnot,AttributesCurr=ReadlinesFromInfoFile(fname,"crush_dir")
    print(Attributes,Attributesnot)
    return render(request,"user/Crush_Report.html",{"A1":Attributes,"A2":Attributesnot,"A3":AttributesCurr,"rel_type_text":fname}) 

'''
def Add_Pred_at_DB(request,predictions,prediction_data,fname):

    Pred_obj=Predictions()
    Pred_obj.user=request.user
    Pred_obj.Pred_num=predictions
    Pred_obj.Pred_name=fname
    Pred_obj.Pred_type=request.session["pred_type"]
    Pred_obj.Pred_agree="default"
    Pred_obj.Pred_inp_data=str(prediction_data)
    Pred_obj.save()
    
'''    
def Procrelatpred(request):
    opinion=[]
    for i in range (1,12):
        opinion.append(int(request.POST.get('opt'+str(i))))

    person_name=request.POST.get('pername')
    npop= np.array(opinion)
    model=get_Model("model_relation")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_rt",predictions)  
    Attributes,Attributesnot,AttributesCurr=ReadlinesFromInfoFile(fname,"rt_dir")
    return render(request,"user/Relat_Report.html",{"A1":Attributes,"A2":Attributesnot,"A3":AttributesCurr,"rel_type_text":fname}) 
    return HttpResponse("Done") 

def Procfriendpred(request):
    opinion=[]
    for i in range (1,16):
        opinion.append(int(request.POST.get('opt'+str(i))))
    person_name=request.POST.get('pername')
    print("The opinion list",opinion)


    npop= np.array(opinion)

    model=get_Model("model_Friends")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_frd",predictions)  
    Attributes,Attributesnot,AttributesCurr=ReadlinesFromInfoFile(fname,"frd_dir")
    return HttpResponse("Done")
'''
def ReadlinesFromInfoFile(filename,dir):
    export_path = os.path.join(os.getcwd(),"InfoFiles",dir, filename+".txt")
    with open(export_path) as f:
        txt_data=f.read()
    split1_data=txt_data.split("!)(")
    characteristics1=split1_data[3].split("#")
    characteristics2=split1_data[4].split("#")
    characteristics3=split1_data[5].split("#")
    characteristics4=split1_data[6].split("#")
    img_name=split1_data[7]
    title=split1_data[0]
    color=split1_data[1]
    desc=split1_data[2]
    return title,color,desc,characteristics1,characteristics2,characteristics3,characteristics4,img_name
    '''
    with open(export_path) as f:
        lines = [line.rstrip() for line in f]
    export_path2 = os.path.join(os.getcwd(),"InfoFiles",dir, filename+"NOT.txt")
    with open(export_path2) as f:
        linesnot = [line.rstrip() for line in f]    
    export_path3 = os.path.join(os.getcwd(),"InfoFiles",dir, filename+"CURR.txt")
    with open(export_path2) as f:
        linescurr = [line.rstrip() for line in f]    
    return lines,linesnot,linescurr
    '''
    
def getFileNamefromtype(type,pred):

    if type=="_self":
        self_list=["Amazing","Healthy","Good","Bad","Very_Bad"]
        return self_list[pred-1]
    elif type=="_crush":
        crush_list=["Definitely","Should_consider","Should_not_consider","Chances_are_less","Very_less"]
        return crush_list[pred-1]
    elif type=="_fd":
        family_list=["Great","Good","Apathy","Bad"]
        return family_list[pred-1]
    elif type=="_frd":
        friend_list=["True_Friend","Kindered_Spirit","Strategist","Good_Time_Charlie","Situational","Occassional","Leech","Acquaintance"]
        return friend_list[pred-1]
    elif type=="_rt":  
        relat_list=["Amazing","Great","Good","Okayish","Bad","Worse"]
        return  relat_list[pred-1]    
        
def Buddyhp(request):

    return render(request,"user/Buddyhp.html")

def selfpredbud(request):
    request.session["pred_type_cb"]="self"
    return render(request,"user/self_cb.html")

def familypredbud(request):
    request.session["pred_type_cb"]="family"
    return render(request,"user/family_cb.html")

def crushpredbud(request):
    request.session["pred_type_cb"]="crush"
    return render(request,"user/crush_cb.html")   
def relatpredbud(request):
    request.session["pred_type_cb"]="relat"
    return render(request,"user/relat_cb.html")
def friendpredbud(request):
    request.session["pred_type_cb"]="friend"
    return render(request,"user/friend_cb.html")


def checkGibberandGiveResponse(request):


    input_string = request.POST['quest']
    classify_res=classify(input_string)
    if(classify_res>80):
        data={
            'iserror':True,
            'result': "Gibberish Input! Please Enter Valid Input.",
            }
        return JsonResponse(data)
    else:    
        answer_string=getReplyForInputQuest(input_string)
        
        cbqa=CBQAStored()
        cbqa.user=request.user
        cbqa.Quest_text=input_string
        cbqa.Answer_text=answer_string
        cbqa.save()
        
        data={
        'iserror':False,
        'result': answer_string,
        }
        return JsonResponse(data)    
    '''
    print("&&&&&")
    classify_res=classify(input_string)
    if(classify_res>80):
        data={
        'result': "#gibberrish",
        }
        return JsonResponse(data)
    '''    
def check_similarity(sentence):
    pass
def call_get_reply(sentence):
    pass
    reply="mock_reply"
    data={
        'result':reply,

    }
    print("here")
    return JsonResponse(data) 
    '''   
   # list1=[request.session["pred_type"],input_string]
   # similarity=ProcessReply(list1)
    print("length of input string",len(input_string))
    similarity=ProcessReply(request.session["pred_type"],input_string)
    if(similarity<50):
        data={
        'result':'#lessimilar',    
        }
        return JsonResponse(data)
    #reply= Predict_from_cb(request.session["pred_type"],input_string)
    reply="mock_reply"
    data={
        'result':reply,

    } 
    return JsonResponse(data)
    '''
   



def selfpred(request):
    request.session["pred_type"]="self"
    return render(request,'user/Self_modal_q_tp.html')    

def familypred(request):
    request.session["pred_type"]="family"
    return render(request,"user/family_modal_q_tp.html")    

def crushpred(request):
    request.session["pred_type"]="crush"
    return render(request,"user/Crush_modal_qp_tp.html")  

def relatpred(request):
    request.session["pred_type"]="relat"
    return render(request,"user/relationships_modal_q_tp.html")  

def friendpred(request):
    request.session["pred_type"]="friend"
    return render(request,"user/friend_modal_q_tp.html")


### Below are the new modifications.

def selfpred_json(request):
    request.session["user_values_arr"]=request.POST.getlist("arrayval[]")
    request.session["per_name"]="SELF"
    return JsonResponse({"success":"true"})
    
def friendpred_json(request):
    request.session["user_values_arr"]=request.POST.getlist("arrayval[]")
    request.session["per_name"]=request.POST.get("pred_name")
    return JsonResponse({"success":"true"})

def relationpred_json(request):
    request.session["user_values_arr"]=request.POST.getlist("arrayval[]")
    request.session["per_name"]=request.POST.get("pred_name")
    return JsonResponse({"success":"true"})

def familypred_json(request):
    request.session["user_values_arr"]=request.POST.getlist("arrayval[]")
    request.session["per_name"]=request.POST.get("pred_name")
    
    return JsonResponse({"success":"true"})

def crushpred_json(request):
    request.session["user_values_arr"]=request.POST.getlist("arrayval[]")
    request.session["per_name"]=request.POST.get("pred_name")
    return JsonResponse({"success":"true"})
    
def save_reaction(request):
    print("dammit")
    print(request)
    print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    return JsonResponse({"success":"true"})
    

def Procselfpred(request):
    
    opinion=[]
    values_arr=request.session["user_values_arr"]
    for i in range (0,8):
        print(opinion)
        print()
        opinion.append(int(values_arr[i]))
    npop= np.array(opinion)
    model=get_Model("model_self")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_self",predictions)
    Title,color,desc,Char1,Char2,Char3,Char4,image_nam=ReadlinesFromInfoFile(fname,"self_dir")
def Procfriendpred(request):

    opinion=[]
    values_arr=request.session["user_values_arr"]
    for i in range (0,15):
        print(opinion)
        print()
        opinion.append(int(values_arr[i]))
    npop= np.array(opinion)
    model=get_Model("model_friend")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_friend",predictions) 
    
    Title,Char1,Char2,Char3,Char4,color,image_name=ReadlinesFromInfoFile(fname,"friend_dir")
def Procrelatpred(request):

    opinion=[]
    values_arr=request.session["user_values_arr"]
    for i in range (0,11):
        print(opinion)
        print()
        opinion.append(int(values_arr[i]))
    npop= np.array(opinion)
    model=get_Model("model_relation")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_relation",request.session["per_name"],) 
    Title,color,desc,Char1,Char2,Char3,Char4,image_nam=ReadlinesFromInfoFile(fname,"relat_dir")
def Proccrushpred(request):

    opinion=[]
    values_arr=request.session["user_values_arr"]
    for i in range (0,5):
        print(opinion)
        print()
        opinion.append(int(values_arr[i]))
    npop= np.array(opinion)
    model=get_Model("model_crush")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    fname=getFileNamefromtype("_crush",predictions)
    Title,color,desc,Char1,Char2,Char3,Char4,image_nam=ReadlinesFromInfoFile(fname,"crush_dir")    
def Procfamilypred(request):

    opinion=[]
    values_arr=request.session["user_values_arr"]
    for i in range (0,4):
        print(opinion)
        print()
        opinion.append(int(values_arr[i]))
    npop= np.array(opinion)
    model=get_Model("model_family")
    Data=TransformData(npop) 
    predictions=get_Predictions(model,Data)
    print(predictions)
    fname=getFileNamefromtype("_fd",predictions) 
    print(fname,"filename")
   # title,color,desc,characteristics1,characteristics2,characteristics3,characteristics4,img_name
    Title,color,desc,Char1,Char2,Char3,Char4,image_name=ReadlinesFromInfoFile(fname,"fd_dir") 
    print(Title,color,desc,Char1,Char2,Char3,Char4,image_name) 
    print(Char1)
    Add_Pred_at_DB_details(request,request.session["per_name"],opinion,predictions,predictions,fname)
    return render(request,'user/fin_rep_grid.html',{'title':Title,'color':color,'Desc':desc,'char1':Char1,'char2':Char2,'char3':Char3,'char4':Char4,'IMG_NAME':'gif_images/nice.gif'})

def Add_Pred_at_DB_details(request,fname,values_arr,Data,predictions,predname):
    request.session["PRED_ID"]=None
    Pred_obj=Predictions()
    Pred_obj.user=request.user
    Pred_obj.Pred_num=predictions
    Pred_obj.Pred_name=predictions
    Pred_obj.Pred_type=request.session["pred_type"]
    Pred_obj.Pred_inp_str=str(values_arr)
    Pred_obj.Person_name=fname
    print(Data)
    Pred_obj.Pred_number=int(Data)
    Pred_obj.save()
    print("asdddddd");
    print(Pred_obj.Pred_id,"<<<<<<<<<<<<<<<<<")
    request.session["PRED_ID"]=Pred_obj.Pred_id
    print("asdddddd");
    
def update_reaction(request):
    reaction_value=request.POST.get("reaction_value")
    #TODO UPDATE THE REACTION
    print(reaction_value,"(((((((((((((((((((")
    print("&&&&&&&&&&&&&&&&&**&)))))))))))))0")
    Predictions.objects.filter(Pred_id=request.session["PRED_ID"]).update(Pred_reaction=reaction_value)
    return JsonResponse({"success":"true"})
def Get_past_Record(request):
    pass
def Get_Previous_predictions(request):
    Pred_list=Predictions.objects.filter(user=request.user)
    return render(request,"user/Predictions.html",{"list":Pred_list})
def Open_Chat_Bot(request):
    
    print("inside bootschat call")
    return render(request,"user/bootsChat.html")
def App_logout(request):
    logout(request)
    return redirect('homepage')

def share_feedback(request):
    return render(request,'user/Feedback.html');
    
def save_feedback(request):
    if request.method=="POST":
        reaction1=request.POST.get('optradio1','')
        reaction2=request.POST.get('optradio2','')
        advice=request.POST.get('advice','')
        user=models.ForeignKey(User,on_delete=models.CASCADE)

        feedback=Feedback()
        feedback.user=request.user
        feedback.Quest_make=reaction1
        feedback.Reaction_thought=reaction2
        feedback.Advice=advice
        
        feedback.save()
        
        
    return HttpResponse("We are Done")    