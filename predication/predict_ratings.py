'''
# ensure the following packages are installed
pip3 install pandas
pip3 install numpy
pip3 install scipy
pip3 install sklearn
pip3 install matplotlib
'''

import pandas as pd
import numpy as np
from matplotlib import pylab as pl
from sklearn.neighbors.classification import KNeighborsClassifier

# %matplotlib inline

ccdefault = pd.read_csv('all_ratings.txt')

list(ccdefault.columns.values)

ccd = ccdefault
list(ccd.columns.values)

#split data
test_idx = np.random.uniform(0, 1, len(ccd)) <= 0.333
train = ccd[test_idx == True]
test = ccd[test_idx == False]

type(train)
train.head()

features = ['movie_id', 'user_id']

n = 2

clf = KNeighborsClassifier(n_neighbors = n)
clf = clf.fit(train[features], train['rating'])
preds = clf.predict(test[features])

# accuracy = sklearn.metrics.accuracy_score(np.asarray(clf), np.asarray(preds))
accuracy = np.where(preds == test['rating'], 1, 0).sum() / float(len(test))*100
print('Neighbors: %d, Accuracy: %2d%%' % (n, round(accuracy, 2))) #Print Accuracy