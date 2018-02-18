import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS

val userRecsK = 5 // top k recommended movies for a user
val movieRecsK = 5 // top k recommended users for a movie

val ratings = spark.read.format("csv").option("header","true").option("inferSchema", true).csv("../data/ratings.csv")

val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

// Build the recommendation model using ALS on the training data
val als = new ALS().setMaxIter(5).setRegParam(0.01).setUserCol("userId").setItemCol("movieId").setRatingCol("rating")

val model = als.fit(training)

// Evaluate the model by computing the RMSE on the test data
// Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
model.setColdStartStrategy("drop")
val predictions = model.transform(test)

val evaluator = new RegressionEvaluator().setMetricName("rmse").setLabelCol("rating").setPredictionCol("prediction")

// Generate top 10 movie recommendations for each user
val userRecs = model.recommendForAllUsers(userRecsK)
userRecs.show(10, false)

// Generate top 10 user recommendations for each movie
val movieRecs = model.recommendForAllItems(movieRecsK)
movieRecs.show(10, false)

val RMSE = evaluator.evaluate(predictions) // Root Mean Square Error
