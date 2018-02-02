import kafka
import time

kafka_server = 'node1:9092' # node1: the kafka server name or actual IP address
producer = kafka.KafkaProducer(bootstrap_servers = kafka_server)

file_name = '../data/movies.csv'
topic_name = 'movie_title_year_cs502'

with open(file_name, 'r') as data:
    next(data) # skip the header
    for movie in data:
        first_comma_index = movie.find(',')
        movie_id = movie[:first_comma_index]
        movie_info = movie[first_comma_index + 1:-2]
        print movie_id, movie_info
        producer.send(topic = topic_name, value = movie_info, key= movie_id, timestamp_ms=time.mktime(time.gmtime()))
 
producer.flush()
producer.close(timeout=30)
