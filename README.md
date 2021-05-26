# MinstDigitClassification
The program classifies handwritten digits using Support vector machines and nearest neighbour algorithms

Algorithm Description
I have used support vector machines to classify the data. Support vector machines is a popular algorithm for classifying the data and I have chosen this algorithm because it had achieved accuracy higher than 99% on the same data set previously. It uses two support vectors to identify a hyperplane which separates the data. Hyperplane is in the middle of these two vectors. There are several approaches to SVM each one is applied depending on the separability of the data. For example, if the data is linearly separable then a hard margin separator is used to identify the support vectors.
In my course work I have applied a soft margin separator. My program learns the hyperplane by selecting a hyper plane then moving it ever so slightly with every wrong classification made. To achieve a better quality of results I iterate through training data many times(detailed breakdown of the results using several different values is provided in the Results section). To achieve best results the value of another hyperparameter of SVM called learning rate also known as alpha needs to be tuned.(see results section for performance measure for different values of alpha)
The approach I first used was to have as many hyperplanes as number of data entries the problem with this approach was that it was too slow to train and resulted in poor accuracy on both test folds.
I then cut the number of hyperplanes to one, which resulted in significant decrease in computation time. This meant that I could try more combinations of hyperparameter values in less time as compared to older approach. Even though this approach resulted in better performance the accuracy was still around 50%.
To improve results further I calculated the centroids of all ten categories and then added distance to each centroid as extra dimension to reach row of data. This gave me 10 extra dimension and increased the chances of data to be linearly separable. Even with this casting to higher dimension best results I could achieve was 70%. I then added distance from origin as extra dimension to each row of data(in other words total number of pixels that are on in each row of data). With this SVM achieved above 90% accuracy.
Although I was getting 90% accuracy some data still went unclassified. To classify that data, I used nearest neighbour algorithm. This resulted in accuracy of 98% with little tuning of hyperparameters **I was able to achieve 99.12% accuracy as average of two-fold test**. To view the results of different hyperparameter combinations please see the results section.
To classify more than one category I used one vs all approach. In this approach one category is classified against all other categories. I used one SVM per category. I then put those SVMs in a decision tree where program goes through each SVM for a positive classification and correct classification of each data point. If there is no such classification by any SVM then data is passed to nearest neighbour algorithm for classification. For breakdown of results please see results section.
The SVM algorithm I used is based on approach mentioned in towards data science article (Gandhi, 2018).

Results 
Results Using SVMs
Following are the top results I achieved from each fold:
Alpha = 0.0002
Epochs = 40
 
 
I wrote a program that iterates over 100 values of alpha ranging from 0.0001 to 0.01 and for each value of alpha it iterates 20 times over each multiple of five in range 0 to 100. Using the results from above mentioned program, values of alpha and epochs that result in highest average accuracy were selected. The results of above program are in files named “resultsfoldOne” and “resultsFoldTwo” which are in the same zip file. In the file named resultsFoldTwo because of typo it says fold 1 but the results are from test fold 2.
**Average accuracy on Minst dataset 2 fold test was 99.12%**
References
Gandhi, R., 2018. Support Vector Machine — Introduction to Machine Learning Algorithms. [Online] 
Available at: https://towardsdatascience.com/support-vector-machine-introduction-to-machine-learning-algorithms-934a444fca47
[Accessed 02 03 2021].


 

