## Data Pre-processing

### Movie Basic information

* location: /data/movie_titles.txt
* format: movie_id, release_date, movie_name
  > Example: 17,2005,7 Seconds
* processing: extract the movie id as the key in kafka message, and the value if the "movie_year,name"

### Rating Information

* location: /data/movie_[id].txt
* format:  
  movie_id:  
  user_id,rating,date
  For example:
  >571:  
  1026389,4,2005-07-06
* processing: kafka message value=1026389,4,2005-07-06; key=571

## Kafka Messages

#### topics: movie_title_year_cs502, input_cs502
#### source codes: /ingestion/movie.py, rating.py
#### setup:

1. Create the topics.

> ./kafka-console-consumer.sh --zookeeper cs502-kafka-node1:2181 --topic input_cs502
> ./kafka-console-consumer.sh --zookeeper cs502-kafka-node1:2181 --topic movie_title_year_cs502


3. Monitor Topic `movie_title_year_cs502`

> ./kafka-console-consumer.sh --bootstrap-server cs502-kafka-node1:9092,cs502-kafka-node2:9092,cs502-kafka-node3:9092 --topic movie_title_year_cs502 --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true

Run the python code `python movie.py` to produce movie messages. The produced messages will show in the console.

3. Start to monitor topic `input_cs502`:

> ./kafka-console-consumer.sh --bootstrap-server cs502-kafka-node1:9092,cs502-kafka-node2:9092,cs502-kafka-node3:9092 --topic input_cs502 --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true

Run the python code `python rating.py` to produce movie messages. The produced messages will show in the console.

Note: Add parameter `--from-beginning` to check all the messages from the beginning.
