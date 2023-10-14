import csv
import datetime
import json
from elasticsearch import Elasticsearch
from elasticsearch import helpers

def format_timestamp(time):
    time = time.strip()
    if(time == ""):
        return None
    formatted_time = datetime.datetime.strptime(time, "%m/%d/%Y %H:%M:%S")
    return formatted_time.strftime("%m/%d/%Y %H:%M:%S")

def is_float(num):
    try:
        float(num)
        return True
    except ValueError:
        return False

def format_number(no):
    no = no.strip()
    if(not is_float(no)):
        return 0
    return float(no)

def format_string(s):
    return s.strip()

formatted_data = []
es_client = Elasticsearch(["http://0.0.0.0:9200"])


with open("input_data/salary_survey-1.csv", "r") as fp:
    
    reader = csv.DictReader(fp)
    for line in reader:
        formatted_obj = {}
        formatted_obj["timestamp"] = format_timestamp(line["Timestamp"])
        if(not formatted_obj["timestamp"]):
            continue
        formatted_obj["employer"] = format_string(line["What industry do you work in?"])
        formatted_obj["location"] = format_string(line["Where are you located? (City/state/country)"])
        formatted_obj["title"] = format_string(line["Job title"])
        formatted_obj["gender"] = ""

        
        annual_base_pay = format_number(line["What is your annual salary?"])
        formatted_obj["salary"] = annual_base_pay

        if formatted_obj["salary"] == 0:
            continue

        formatted_data.append(formatted_obj)
        es_client.index(index="work", document=formatted_obj)
