# Movie Recommendation Using Spark MLlib

## Recommender Systems

[Wikipedia defination](https://en.wikipedia.org/wiki/Recommender_system#Collaborative_filtering)

## Algorithm

[alternating least squares (ALS)](http://dl.acm.org/citation.cfm?id=1608614)

## Spark MLlib Reference

[Collaborative Filtering](https://spark.apache.org/docs/2.2.0/ml-collaborative-filtering.html#collaborative-filtering-1)

## Dataset

[MovieLens Latest Datasets](https://grouplens.org/datasets/movielens/latest/)

The small dataset is for development, and the full one is for research.

## Source Code

../recommendation/movielens.scala

Note: The reference code is [Build and use ALS model in Scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/ml/ALSExample.scala)

## Running Result

```scala
scala> :load movielens.scala
Loading movielens.scala...
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
userRecsK: Int = 3
movieRecsK: Int = 3
ratings: org.apache.spark.sql.DataFrame = [userId: int, movieId: int ... 2 more fields]
training: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [userId: int, movieId: int ... 2 more fields]
test: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [userId: int, movieId: int ... 2 more fields]
als: org.apache.spark.ml.recommendation.ALS = als_596a949eba6a
model: org.apache.spark.ml.recommendation.ALSModel = als_596a949eba6a
res53: model.type = als_596a949eba6a
predictions: org.apache.spark.sql.DataFrame = [userId: int, movieId: int ... 3 more fields]
evaluator: org.apache.spark.ml.evaluation.RegressionEvaluator = regEval_91666d86f1c7
userRecs: org.apache.spark.sql.DataFrame = [userId: int, recommendations: array<struct<movieId:int,rating:float>>]
+------+-------------------------------------------------------+                
|userId|recommendations                                        |
+------+-------------------------------------------------------+
|471   |[[549,5.80754], [3606,5.5475926], [1927,5.471158]]     |
|463   |[[2068,5.22155], [67504,5.17669], [83411,5.17669]]     |
|496   |[[68135,7.860754], [714,7.7964883], [7444,7.076659]]   |
|148   |[[2068,6.479989], [123,6.3140583], [8154,6.267043]]    |
|540   |[[4343,8.54361], [87520,8.4219055], [955,8.362489]]    |
|392   |[[66665,7.458527], [1354,7.166462], [1295,7.132999]]   |
|243   |[[1649,5.3091044], [3742,5.228141], [314,5.1877494]]   |
|623   |[[105504,6.5468974], [1303,6.2240944], [7063,6.058887]]|
|31    |[[1303,6.156767], [900,6.0437346], [73344,5.8090906]]  |
|516   |[[3676,6.0220885], [38886,5.8937864], [1192,5.887353]] |
|580   |[[2936,5.129917], [940,5.057861], [549,5.0057454]]     |
|251   |[[3566,6.639389], [5146,6.593603], [98154,6.4936743]]  |
|451   |[[6773,6.9126067], [1303,6.7874026], [250,6.7527]]     |
|85    |[[6216,9.510381], [3771,8.904471], [900,8.511701]]     |
|137   |[[3015,8.742452], [3430,8.429316], [4467,7.938176]]    |
|65    |[[7013,9.596241], [3672,9.299351], [3581,8.675211]]    |
|458   |[[1243,6.5912123], [1303,6.3636646], [2202,6.2687764]] |
|481   |[[88810,5.6435566], [71899,5.6264067], [922,5.5651746]]|
|53    |[[549,8.368651], [6216,7.9172897], [4965,7.797457]]    |
|255   |[[946,6.4032803], [3067,6.05025], [3022,5.9150405]]    |
+------+-------------------------------------------------------+
only showing top 20 rows

movieRecs: org.apache.spark.sql.DataFrame = [movieId: int, recommendations: array<struct<userId:int,rating:float>>]
+-------+---------------------------------------------------+                   
|movieId|recommendations                                    |
+-------+---------------------------------------------------+
|1580   |[[336,5.9175158], [490,5.444438], [459,5.1164474]] |
|5300   |[[336,9.358697], [112,8.935186], [448,8.450362]]   |
|6620   |[[670,10.433435], [336,9.455859], [131,7.721391]]  |
|7340   |[[112,8.947], [336,7.7480373], [326,6.7813253]]    |
|32460  |[[635,7.6056905], [448,6.0622663], [266,5.896119]] |
|54190  |[[336,15.97729], [55,11.255099], [663,9.760745]]   |
|471    |[[336,7.0981297], [162,6.682485], [395,6.396001]]  |
|1591   |[[59,7.302951], [539,7.003467], [567,6.5760837]]   |
|1342   |[[357,5.7141614], [489,5.51911], [604,5.42447]]    |
|2122   |[[64,6.6362343], [27,6.359438], [116,5.8011856]]   |
|2142   |[[174,7.576265], [663,7.3967667], [498,7.2860103]] |
|7982   |[[112,7.290888], [228,6.8007946], [319,6.468418]]  |
|44022  |[[592,5.9199862], [219,5.7085586], [543,5.5372515]]|
|141422 |[[112,5.312349], [448,4.4993086], [167,4.4430804]] |
|463    |[[490,6.570219], [336,6.568168], [55,5.777037]]    |
|833    |[[448,4.6045256], [296,4.3729653], [112,4.3631]]   |
|5803   |[[310,4.3636403], [112,4.1281447], [331,4.11479]]  |
|7833   |[[261,7.2741404], [288,5.516887], [663,5.490848]]  |
|160563 |[[410,6.4993544], [556,5.258425], [495,5.1853065]] |
|3794   |[[415,7.130268], [354,6.801574], [156,6.6027656]]  |
+-------+---------------------------------------------------+
only showing top 20 rows

RMSE: Double = 1.1265600050309728    
```

Note: For display purpose, I only shwo the top 3 recommended movies, and users.

## Next Steps:

- [ ] read the input data from Cassandra
- [ ] run the program use the full dataset (20 million ratings with 550M file size)
- [ ] put the running result to Kafka topic (optional)





