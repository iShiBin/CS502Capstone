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
import matplotlib
matplotlib.use('agg',warn=False, force=True)
from matplotlib import pyplot as plt
from matplotlib import pylab as pl
from sklearn.neighbors.classification import KNeighborsClassifier
from datetime import datetime
#%matplotlib inline

ccdefault = pd.read_csv('all_ratings_sample.txt')

list(ccdefault.columns.values)

ccd = ccdefault
list(ccd.columns.values)

#split data
test_idx = np.random.uniform(0, 1, len(ccd)) <= 0.2
train = ccd[test_idx == True]
test = ccd[test_idx == False]

type(train)
train.head()

features = ['movie_id', 'date']

for k in (79):
    print('Start to run on features:', features)
    start = datetime.now()
    clf = KNeighborsClassifier(n_neighbors = k)
    clf = clf.fit(train[features], train['rating'])
    preds = clf.predict(test[features])

    # accuracy = sklearn.metrics.accuracy_score(np.asarray(clf), np.asarray(preds))
    accuracy = np.where(preds == test['rating'], 1, 0).sum() / float(len(test))*100
    end = datetime.now()
    print('Neighbors: %d, Accuracy: %2d%%' % (k, round(accuracy, 2))) #Print Accuracy
    print('Finished running using: ', end - start)
