import collections
import kafka
import time
import os, sys
import re
import calendar
import datetime
import random

from datetime import date
from datetime import timedelta
from _collections import defaultdict, deque
from time import mktime, strptime

data_dir = '../data/'
kafka_server = 'cs502-kafka-node1:9092'
topic_name = 'input_cs502'
cut_date = datetime.date(2000, 01, 01)
interval = datetime.timedelta(days=7)

_debug_ = True

def read_rating(file_name):
    with open(file_name, 'r') as data:
        movie_id = next(data)[:-1] #remove the /n
        ratings = {}
        for line in data:
#             if _debug_: print(line[:-1]) #correct from here
            rate_date = line[line.rfind(',')+1:-1] # remove the last /n
            if rate_date not in ratings: ratings[rate_date] = []
            ratings[rate_date].append(movie_id+line[:-1])
    return ratings

def collect_all_rating(data_dir):
    all_ratings = {}
    file_names = [f for f in os.listdir(data_dir) if re.match(r'mv_[0-9]+.*\.txt', f)]
#     file_names = [f for f in os.listdir(data_dir) if re.match(r'mv_00019[0-9]+.*\.txt', f)]
#     file_names = [f for f in os.listdir(data_dir) if f == 'mv_0001905.txt']
#     print(file_names)
    for f in file_names:
        ratings = read_rating(data_dir + f)
        
        for r in ratings:
            if r not in all_ratings:
                all_ratings[r] = ratings[r]
            else:
                all_ratings[r].extend(ratings[r])
    
    return all_ratings

messages = collect_all_rating(data_dir)

mq = deque()
for key in (sorted(messages)):
    mq.extend(messages[key])

print("Total:", len(mq)) # the message number matches in the file

producer = kafka.KafkaProducer(bootstrap_servers = kafka_server)

movie_set = set()

while mq:
    while mq:
        dt_str = mq[0][mq[0].rfind(',')+1:]
        if dt_str > cut_date.__str__(): break
        msg = mq.popleft()
        movie_id, movie_rating = msg[:msg.find(':')], msg[msg.find(':')+2:]
#         print(movie_id, movie_rating)
#         print(dt)
        if _debug_:
            movie_set.add(movie_id)
        dt = time.strptime(dt_str, "%Y-%m-%d")
        producer.send(topic = topic_name, value=movie_rating, key=movie_id, timestamp_ms = calendar.timegm(dt)*1000)
    producer.flush()
    cut_date += interval
    time.sleep(0.5)

print(movie_set)