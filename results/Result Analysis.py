# -*- coding: utf-8 -*-
"""
Created on Mon Feb 26 16:51:34 2018

@author: alexa
"""
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns


# Load xls sheet with data
# doc = xlrd.open_workbook('dsu_clean.xls').sheet_by_index(0)

combined_data_file = open("dsu.txt", "r")
shimmer_data_file = open("shimmer.txt", "r")
dsu_data_file = open("dsuRead.txt")

combined_data = combined_data_file.readlines()
shimmer_data = shimmer_data_file.readlines()
dsu_data = dsu_data_file.readlines()

shimmer_data = list(map(int, shimmer_data))
combined_data = list(map(int, combined_data))
dsu_data = list(map(int, dsu_data))

shimmer_data = np.asarray(shimmer_data)
combined_data = np.asarray(combined_data)
dsu_data = np.asarray(dsu_data)

fig = plt.figure(figsize=(5,5))

sns.distplot(shimmer_data)
plt.axvline(shimmer_data.mean(), color='k', linestyle='dashed', linewidth=1)
plt.text(shimmer_data.mean() + 100, 0.0017, ("Mean: %d" % shimmer_data.mean()))
plt.xlabel('Milliseconds')
plt.show()
fig = plt.figure(figsize=(5,5))
sns.distplot(combined_data)
plt.axvline(combined_data.mean(), color='k', linestyle='dashed', linewidth=1)
plt.text(combined_data.mean() + 200, 0.00085, ("Mean: %d" % combined_data.mean()))
plt.xlabel('Milliseconds')
plt.show()
fig = plt.figure(figsize=(5,5))
sns.distplot(dsu_data)
plt.axvline(dsu_data.mean(), color='k', linestyle='dashed', linewidth=1)
plt.text(dsu_data.mean() + 20, 0.006, ("Mean: %d" % dsu_data.mean()))
plt.xlabel('Milliseconds')
plt.show()
fig = plt.figure(figsize=(5.65,5.65))
sns.boxplot(shimmer_data)
plt.xlabel('Milliseconds')
plt.show()
fig = plt.figure(figsize=(5.65,5.65))
sns.boxplot(combined_data)
plt.xlabel('Milliseconds')
plt.show()
fig = plt.figure(figsize=(5.65,5.65))
sns.boxplot(dsu_data)
plt.xlabel('Milliseconds')
plt.show()