// package cassandra.spark
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.SparkSession
object loaddata {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("Simple Application").config("spark.master", "local").config("spark.cassandra.connection.host", "52.33.132.162").getOrCreate()
    val DF = spark.read.format("org.apache.spark.sql.cassandra").options(Map("keyspace"->"bittiger" , "table"->"movie_cs502")).load
    DF.show(10)
    spark.stop();
  }
}
