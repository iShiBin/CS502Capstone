Predict movie rate information.

# Use KNN Algorithm

Here is the optimal values after trying different features among [movie_id, user_id, date], and `k`:
* features: movie_id, date
* k: 79

The prediction rate is around 36%.

Note:
- The prediction is based on 1 million records. Train data and verification data rate is 8:2.
- The required memory is 32G at minimum to run on the all 7 millions records.

# Use Spark's MLlib...
