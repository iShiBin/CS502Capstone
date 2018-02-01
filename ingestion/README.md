# How to Produce Kafka Messages Using this Program

## Assumption

The topics have already been created by the Kafka administrator. Otherwise, you may need to create these topics using following commands.

```bash
./kafka-console-consumer.sh --zookeeper node1:2181 --topic input_cs502
./kafka-console-consumer.sh --zookeeper node1:2181 --topic movie_title_year_cs502
```

Note: `node1` is the kafka server node in your hosts file. You can use the actual IP address instead.

## Commands

**Movie Messages**: `python movie.py`

**Rating Messages**: `python ratings.py`

## Verfication

You can monitor the message queue in Kafka to confirm whether these messges are sent successfully. 

To monitor the movie topic `movie_title_year_cs502`, run this command:

> ./kafka-console-consumer.sh --bootstrap-server node1:9092,node2:9092,node3:9092 --topic movie_title_year_cs502 --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true

To monitor the rating topic `input_cs502`, run this command:

> ./kafka-console-consumer.sh --bootstrap-server node1:9092,node2:9092,node3:9092 --topic input_cs502 --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true

Note: You can add parameter `--from-beginning` to check all the messages from the beginning.

#Data Format

## Movies Data File Structure (movies.csv)

Movie information is in file `movies.csv`, each line of which after the header row represents one movie, and has the following format:

> movieId,title,genres

Movie titles are entered manually or imported from <https://www.themoviedb.org/>, and include the year of release in parentheses. Errors and inconsistencies may exist in these titles.

Genres are a pipe-separated list, and are selected from the following:

- Action
- Adventure
- Animation
- ...

## Ratings Data File Structure (ratings.csv)

All ratings are contained in the file `ratings.csv`. Each line of this file after the header row represents one rating of one movie by one user, and has the following format:

>  userId,movieId,rating,timestamp

The lines within this file are ordered first by userId, then, within user, by movieId.

Ratings are made on a 5-star scale, with half-star increments (0.5 stars - 5.0 stars).

Timestamps represent seconds since midnight Coordinated Universal Time (UTC) of January 1, 1970.

**Note**: Check the [website](http://files.grouplens.org/datasets/movielens/ml-latest-small-README.html) for complete information regarding to the data format.

# Kafka Messages Description

## Topics: movie_title_year_cs502
This topic is for the movie information (*movies.csv*).

**Message Format:**
- key: movieId
- value: title,genres
- timestamp_ms: millisecond from midnight  January 1, 1970(UTC) to now (generated value)

## input_cs502
This topic is for the rating information (*ratings.csv*).

**Message Format:**

- key: movieId
- value: userId,rating
- timestamp_ms: timestamp * 1000 (transform the timestamp in the file from second to millisecond)

# Change History

2018-02-01: Change the dataset from `nexflix` to `movielens`. Use the "[MovieLens Latest Datasets](https://grouplens.org/datasets/movielens/latest/)" section: [small dataset](http://files.grouplens.org/datasets/movielens/ml-latest-small.zip)  for development and the [full dataset](http://files.grouplens.org/datasets/movielens/ml-latest.zip) for production running.