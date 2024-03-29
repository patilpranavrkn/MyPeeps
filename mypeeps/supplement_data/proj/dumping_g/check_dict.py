import pickle
import os
dict_1={"intents": [
        {"tag": "greeting",
         "patterns": ["Hi", "How are you", "Is anyone there?", "Hello", "Good day"],
         "responses": ["Hello, thanks for visiting", "Good to see you again", "Hi there, how can I help?"],
         "context_set": ""
        },
        {"tag": "goodbye",
         "patterns": ["Bye", "See you later", "Goodbye"],
         "responses": ["See you later, thanks for visiting", "Have a nice day", "Bye! Come back again soon."]
        },
        {"tag": "thanks",
         "patterns": ["Thanks", "Thank you", "That's helpful"],
         "responses": ["Happy to help!", "Any time!", "My pleasure"]
        },
        {"tag": "hours",
         "patterns": ["What hours are you open?", "What are your hours?", "When are you open?" ],
         "responses": ["We're open every day 9am-9pm", "Our hours are 9am-9pm every day"]
        },
        {"tag": "location",
         "patterns": ["What is your location?", "Where are you located?", "What is your address?", "Where is your restaurant situated?" ],
         "responses": ["We are on the intersection of London Alley and Bridge Avenue.", "We are situated at the intersection of London Alley and Bridge Avenue", "Our Address is: 1000 Bridge Avenue, London EC3N 4AJ, UK"]
        },
        {"tag": "payments",
         "patterns": ["Do you take credit cards?", "Do you accept Mastercard?", "Are you cash only?" ],
         "responses": ["We accept VISA, Mastercard and AMEX", "We accept most major credit cards"]
        },
        {"tag": "todaysmenu",
         "patterns": ["What is your menu for today?", "What are you serving today?", "What is today's special?"],
         "responses": ["Today's special is Chicken Tikka", "Our speciality for today is Chicken Tikka"]
        },
        {"tag": "deliveryoption",
         "patterns": ["Do you provide home delivery?", "Do you deliver the food?", "What are the home delivery options?" ],
         "responses": ["Yes, we provide home delivery through UBER Eats and Zomato?", "We have home delivery options through UBER Eats and Zomato"],
         "context_set": "food"
        },
        {"tag": "menu",
         "patterns": ["What is your Menu?", "What are the main course options?", "Can you tell me the most delicious dish from the menu?", "What is the today's special?"],
         "responses": ["You can visit www.mymenu.com for menu options", "You can check out the food menu at www.mymenu.com", "You can check various delicacies given in the food menu at www.mymenu.com"],
         "context_filter": "food"
        }
   ]
}

print("WILL Dump now")
pickle.dump(dict_1,open('sample_int.p','wb'))
print("Dumped")
print("The current dir is ",os.getcwd())

df2=pickle.load(open('sample_int.p','rb'))
    
print("Read")
print(df2)
   