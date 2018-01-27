//Enable Cassandra-specific functions on the SparkContext, SparkSession, RDD, and DataFrame:

import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._

//Use the sc.cassandraTable method to view this table as a Spark RDD:
val rdd = sc.cassandraTable("bittiger", "movie_cs502")
println(rdd.count)
println(rdd.first)
//rdd.foreach(println)//print the table


//Top 10 rated movie in given month
val sqlContext = new  org.apache.spark.sql.SQLContext(sc)
import sqlContext.implicits._ //convert implicitly an RDD to DataFrame
rdd.toDF()//<console>:37: error: value toDF is not a member of com.datastax.spark.connector.rdd.CassandraTableScanRDD[com.datastax.spark.connector.CassandraRow]

//https://docs.datastax.com/en/datastax_enterprise/4.8/datastax_enterprise/spark/sparkSCcontext.html

val firstRow = rdd.first
//firstRow: com.datastax.spark.connector.CassandraRow = CassandraRow{movie_id: 16879, start_time: 2005-12-28 19:00:00-0500, count: 331, movie_title: Titanic, rating: 3.836858, release_year: 1997}

firstRow.get[Int]("movie_id")
//res7: Int = 16879

//load the data in a data frame !!!
val empDF = spark.read.format("org.apache.spark.sql.cassandra").options(Map("keyspace"->"bittiger" , "table"->"movie_cs502")).load
// https://bigdataclassmumbai.com/integrating-apache-spark-apache-cassandra/

