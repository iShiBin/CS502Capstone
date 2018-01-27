//Enable Cassandra-specific functions on the SparkContext, SparkSession, RDD, and DataFrame:

import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._

//Use the sc.cassandraTable method to view this table as a Spark RDD:
val rdd = sc.cassandraTable("bittiger", "movie_rating_cs502")
println(rdd.count)
println(rdd.first)
RDD.collect().foreach(println) // print the whole table
//println(rdd.map(_.getInt("movie_id")).sum)  
