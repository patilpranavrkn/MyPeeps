import math
import re
from collections import Counter
WORD = re.compile(r"\w+")
def ReadlinesFromQuestFile(filename,sentence):
    export_path = os.path.join(os.getcwd(),"QuestFiles", filename+"QF.txt")
    with open(export_path) as f:
        lines = [line.rstrip() for line in f]    
    
    for sentiter in lines:
        simval=proc_similarity(sentiter,sentence)
        if simval >50:
            return simval
            break
    
    return -1    
def ProcessReply(list1):
    return ReadlinesFromQuestFile(list1[0],list1[1])



def proc_similarity(sentence1,sentence2):
    vector1 = text_to_vector(sentence1)
    vector2 = text_to_vector(sentence2)

    cosine = get_cosine(vector1, vector2)
    

def get_cosine(vec1, vec2):
    intersection = set(vec1.keys()) & set(vec2.keys())
    numerator = sum([vec1[x] * vec2[x] for x in intersection])

    sum1 = sum([vec1[x] ** 2 for x in list(vec1.keys())])
    sum2 = sum([vec2[x] ** 2 for x in list(vec2.keys())])
    denominator = math.sqrt(sum1) * math.sqrt(sum2)

    if not denominator:
        return 0.0
    else:
        return float(numerator) / denominator


def text_to_vector(text):
    words = WORD.findall(text)
    return Counter(words)



