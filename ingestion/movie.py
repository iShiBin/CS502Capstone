import kafka
from datetime import datetime
import time

file_name = '../data/movie_titles.txt'
kafka_server = 'cs502-kafka-node1:9092'
topic_name = 'movie_title_year_cs502'

producer = kafka.KafkaProducer(bootstrap_servers = kafka_server)

with open(file_name, 'r') as data:
    for msg in data:
        producer.send(topic = topic_name, value = msg, timestamp_ms=time.mktime(time.gmtime()))
#         time.sleep(1)

producer.flush()
producer.close(timeout=30)