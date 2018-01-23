import os, sys
import re

data_dir = '../data/'
output_file = open('all_ratings.txt', 'w')
output_file.write('movie_id,user_id,rating,date\n')

def collect_ratings(file_name):
    with open(file_name, 'r') as data:
        movie_id = next(data)[:-2] #remove the /n
        for line in data:
            user_id, value, date = line.split(',')
            date = date[:4]+date[5:7]+date[8:]
            line = ','.join((movie_id,user_id,value,date))
            output_file.write(line)

file_names = [f for f in os.listdir(data_dir) if re.match(r'mv_[0-9]+.*\.txt', f)]
# file_names = [f for f in os.listdir(data_dir) if re.match(r'mv_000190[0-9]+.*\.txt', f)]
for f in file_names:
    collect_ratings(data_dir + f)
    
output_file.close()