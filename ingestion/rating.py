import collections
import kafka
from datetime import date
from datetime import timedelta
import time

from _collections import defaultdict, deque

with open('mv_0006972.txt', 'r') as data:
    movie_id = next(data)[:-2] +',' # remove the last char :
#     print(movie_id)
    messages = defaultdict(list)
    for line in data:
        rate_date = line[line.rfind(',')+1:]
#         print(rate_date)
        messages[rate_date[:-1]].append(movie_id+line[:-1])
#     print(messages)

data.close()

mq = deque()
for key in (sorted(messages)):
    mq.extend(messages[key])

target = '2004-04-22'
# while mq:
#     dt = mq[0][mq[0].rfind(',')+1:]
#     if dt < target:
#         print(mq.popleft())
#     else:
#         break
cut_date = date(2000, 01, 01)
interval = timedelta(days=1)
# print(cut_date.__str__())


producer = kafka.KafkaProducer(bootstrap_servers = 'localhost:9092')
while mq:
    while True:
        dt = mq[0][mq[0].rfind(',')+1:]
        if dt > cut_date.__str__(): break
        msg = mq.popleft()
        producer.send(topic='ratings', value=msg)
#         print(msg)
    
#     producer.flush()
    cut_date += interval
    time.sleep(1)
