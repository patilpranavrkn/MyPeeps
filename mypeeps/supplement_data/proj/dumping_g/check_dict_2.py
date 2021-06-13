import pickle
import os

dict_1={"intents": [
    {"tag": "Greetings",
     "patterns": ["Hi", "How are you", "Is anyone there?", "Hello", "Good day"],
     "responses": ["Hello, thanks for visiting", "Good to see you !", "Hi there, how can I help?","How Can I help with your self"],
     "context_set": ""
    },
    {
    "tag": "IMPRESS",
     "patterns": ["How to impress my crush?","If the person is put of my league how to get them? ","How to make her mine?","How to make him mine?"],
     "responses": ["Focus on your life","Try improving your life","Work on yourself","Improve yourself in each and every aspect","Try to reach your potential"],
     "context_set": ""
    },  
    {
        "tag": "APPROACHSING",
         "patterns": ["Should I make a move if she is single?","Should I make a move if he is single?","how to make a move?","Make me notice in front of him","Make me notice in front of her"],
         "responses": ["You should be careful","Go slow and steady"],
         "context_set": ""
    },
    {
        "tag": "APPROACHRELAT",
         "patterns": ["Should I make a move if she is relationship?","Should I make a move if he is relationship?","She is in Relationship","She has a boyfriend","He has a girlfriend","He is committed","She is committed"],
         "responses": ["No you shouldn't do it","It will seem a very desperate act","No you should find someone other"],
         "context_set": ""
    },       
    {
        "tag": "AVOIDS",
         "patterns": ["What to do if they avoid me?","What to do if he avoids me?","What to do if she avoids me?"],
         "responses": ["Don't chase them","You might seem desperate","If you're being avoided avoid them back."],
         "context_set": ""
    },    
    {
        "tag": "SIGNS",
         "patterns": ["What are some hints to look out for?","He is giving hints?","She is giving hints?"],
         "responses": ["Getting attention from them","They will speak with you"],
         "context_set": ""
    }
 
    
    
    ]
}

print("WILL Dump now")
pickle.dump(dict_1,open('sample_int_crush.p','wb'))
print("Dumped")
print("The current dir is ",os.getcwd())

intents=pickle.load(open('sample_int_crush.p','rb'))
    
print("Read")
print(intents)