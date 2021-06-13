from tensorflow.keras.models import load_model
import pickle
import random
import nltk
nltk.download('punkt')
nltk.download('wordnet')
from nltk.stem import WordNetLemmatizer
import numpy as np

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
print(res)
def getReplyForInputQuest(inputquest):
    initialize()
    ints=predict_class(inputquest)
    res=get_response(ints,intents)
    return res