import collections
import kafka
from datetime import date
from datetime import timedelta
import time
from _collections import defaultdict, deque
import os, sys
import re

data_dir = '../data/'
kafka_server = 'cs502-kafka-node1:9092'
topic_name = 'input_cs502'
cut_date = date(2000, 01, 01)
interval = timedelta(days=1)

debug = True

def read_rating(file_name):
    with open(file_name, 'r') as data:
        movie_id = next(data) #including the ending ':'
    #     print(movie_id)
        ratings = defaultdict(list)
        for line in data:
            rate_date = line[line.rfind(',')+1:]            
            ratings[rate_date[:-1]].append(movie_id+line[:-1])
    #     print(ratings)
    return ratings


def collect_all_rating(data_dir):
    all_ratings = defaultdict(list)
#     file_names = [f for f in os.listdir(data_dir) if re.match(r'mv_[0-9]+.*\.txt', f)]
    file_names = [f for f in os.listdir(data_dir) if f == 'mv_0011064.txt']
#     print(file_names)
    for f in file_names:
        all_ratings.update(read_rating(data_dir + f))
    
    return all_ratings
#     for e in all_ratings:
#         print(e, all_ratings[e])

messages = collect_all_rating(data_dir)

mq = deque()
for key in (sorted(messages)):
    mq.extend(messages[key])

producer = kafka.KafkaProducer(bootstrap_servers = kafka_server)

while mq:
    while mq:
        dt = mq[0][mq[0].rfind(',')+1:]
        if dt > cut_date.__str__(): break
        msg = mq.popleft()
        movie_id, movie_rating = msg[:msg.find(':')], msg[msg.find(':')+1:]
        producer.send(topic = topic_name, value=movie_rating, key=movie_id, timestamp_ms=time.mktime(time.gmtime()))
#         print(msg)
    producer.flush()
    cut_date += interval
    time.sleep(1)