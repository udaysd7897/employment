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


with open("input_data/salary_survey-3.csv", "r") as fp:
    
    reader = csv.DictReader(fp)
    for line in reader:
        formatted_obj = {}
        formatted_obj["timestamp"] = format_timestamp(line["Timestamp"])
        if(not formatted_obj["timestamp"]):
            continue
        formatted_obj["employer"] = format_string(line["Employer"])
        formatted_obj["location"] = format_string(line["Location"])
        formatted_obj["title"] = format_string(line["Job Title"])
        formatted_obj["gender"] = format_string(line["Gender"])

        
        annual_base_pay = format_number(line["Annual Base Pay"])
        signing_bonus = format_number(line["Signing Bonus"])
        annual_bonus = format_number(line["Annual Bonus"])
        annual_stock_value = format_number(line["Annual Stock Value/Bonus"])

       
        formatted_obj["salary"] = annual_base_pay + signing_bonus + annual_bonus + annual_stock_value

        if formatted_obj["salary"] == 0:
            continue
        formatted_data.append(formatted_obj)
        es_client.index(index="work", document=formatted_obj)
