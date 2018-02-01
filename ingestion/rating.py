import kafka
import time
from collections import defaultdict

kafka_server = 'node1:9092'
rating_file = '../data/ratings.csv'
topic_name = 'input_cs502'
interval = 1 # sleep time in second
batch_size = 3600 # how many seconds as a batch

def read_ratings(file_name):
    ratings = defaultdict(list)
    with open(file_name, 'r') as data:
        next(data) # skip the header
        for row in data:
            comma_index = row.rfind(',')
            sec = int(row[comma_index+1:-2])
            ratings[sec].append(row[:comma_index])
#     print ratings
    return ratings

producer = kafka.KafkaProducer(bootstrap_servers = kafka_server)

def send_messages(messages = read_ratings(rating_file)):
    if not messages: return
    batch_cut =  ( int(min(messages)) // batch_size ) * batch_size 
    for sec in (sorted(messages)):
#         print sec
#         print messages[sec]
        for msg in messages[sec]:
            user_id, movie_id, rating = msg.split(',')
            msg_key = movie_id
            msg_value = user_id + ',' + rating
            producer.send(topic = topic_name, value=msg_value, key=msg_key, timestamp_ms = sec*1000)
        
        if sec > batch_cut:
            time.sleep(interval)
            batch_cut += batch_size # next batch cut

send_messages()
producer.flush()
producer.close(timeout=30)