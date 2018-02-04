First, kill any running instance:

ps aux | grep uber-cs502-capstone-0.0.1-SNAPSHOT.jar

kill -9 process-id

Second, reset streaming app history

kafka-streams-application-reset.sh --application-id netflix-analysis --input-topics input_cs502,movie_title_year_cs502 --intermediate-topics rekeyed-topic --bootstrap-servers node1:9092,node2:9092,node3:9092 --zookeeper node1:2181,node2:2181,node3:2181/kafka

Please run the following command

java -cp uber-cs502-capstone-0.0.1-SNAPSHOT.jar App.Demo
