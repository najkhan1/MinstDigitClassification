package digitrecognition;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * object of this class can train and test data using
 * SVM
 */
public class SVM extends Thread {

    // training set
    ArrayList<ArrayList<Double>> trainPoints;
    //test set
    ArrayList<ArrayList<Double>> testPoints;
    // training data in a different format for centroid calculation
    ArrayList<ArrayList<ArrayList<Double>>> trainCategories = new ArrayList<>();
    // test data in a different format for centroid calculation
    ArrayList<ArrayList<ArrayList<Double>>> testCategories = new ArrayList<>();
    // centroids of test sets
    ArrayList<double[]> centeriodsTest = new ArrayList<>();
    // centroids of training set
    ArrayList<double[]> centeroidTrain = new ArrayList<>();
    // arraylist of containing weights
    ArrayList<Double> singleWeights;
    // category SVM classifies vs all other categories
    int classifierID;
    // starting number of epochs. its the number of passes that will be performed
    // over whole training dataset
    double epochs = 1;
    // learning rate of SVM
    double alpha = Main.ALPHA;

    // constructor for the class that takes in category it will classify as parameter
    public SVM(int dimensionConsidered) {
        //load training data from dataset
        trainPoints = fileReader(Main.FILEPATHTRAIN, dimensionConsidered);
        //load test data from dataset
        testPoints = fileReader(Main.FILEPATHTEST, dimensionConsidered);
        //initialize weights to 0
        singleWeights = fillZero(trainPoints.size() - 1 + 11);
        // centroids of training and test data
        centeroidTrain = calculateCenteroids(fileReader(Main.FILEPATHTRAIN, trainCategories));
        centeriodsTest = calculateCenteroids(fileReader(Main.FILEPATHTEST, testCategories));
        // category SVM will classify vs all other categories
        classifierID = dimensionConsidered;
        //add 10 extra dimension to training set based on distance from each centroid
        castToHigherDimension(true);
        //add 10 extra dimension to test set based on distance from each centroid
        castToHigherDimension(false);
        // add extra dimension to training set based on total number of pixels on in
        // each row of data
        trainPoints = distanceFromCenter(trainPoints, false);
        // add extra dimension to test set based on total number of pixels on in
        // each row of data
        testPoints = distanceFromCenter(testPoints, true);
    }

    // train the SVM
    public void svmTrainTwo() {
        // keep iterating until desired number of epochs is reach
        while (epochs < Main.EPOCHS) {
            // loop over each row in the training dataset
            for (int index = 0; index < trainPoints.get(0).size(); index++) {
                // make prediction by summing the product of each weight and its
                // respective dimension as shown in equation below
                // y = w1*Dimension1 + w2*dimension2 + ... wn*Dimension-n
                double summation = updateSinglePrediction(rowOfData(index, false));
                // mulitpy the summation to each target value
                double product = summation * trainPoints.get(trainPoints.size() - 1).get(index);
                if (product >= 1) {
                    //update weight using following equation
                    //weights = for each weight(w-alpha*(2*1/epochs * w)
                    singleWeights = updateWForGreaterThenOne(epochs);
                } else {
                    // update the weights using following equation
                    //weights = for each weight(Wn + alpha*(Dimension-n-value * target-value - 2*1/epochs*Wn))
                    singleWeights = updateWForLessThenOne(epochs, rowOfData(index, false));
                }
            }
            epochs++;
        }
    }

    // classify the data
    public double predict(int index) {
        // make classification
        double predictTarget = updateSinglePrediction(rowOfData(index, true));
        //check if classification is positive and correct if it is return 1 else
        //return -1
        if (predictTarget >= 1 && testPoints.get(testPoints.size() - 1).get(index) == 1) {
            return 1;
        } else {
            return -1;
        }
    }

    // overriding built in method to run training on separate thread
    @Override
    public void run() {
        // train the SVM
        svmTrainTwo();
    }

    //parse row of data from test set or training based on boolean value passed in
    //as parameter
    public ArrayList<Double> rowOfData(int indexOfRow, boolean test) {
        // row of data to be returned by method
        ArrayList<Double> tempArrayToHoldData = new ArrayList<>();
        // if boolean test is false get data from training data set
        if (!test) {
            for (int index = 0; index < trainPoints.size(); index++) {
                tempArrayToHoldData.add(trainPoints.get(index).get(indexOfRow));
            }
            // else get data from test set
        } else {
            for (int index = 0; index < testPoints.size(); index++) {
                tempArrayToHoldData.add(testPoints.get(index).get(indexOfRow));
            }
        }
        // return row of data
        return tempArrayToHoldData;
    }

    // this method sums the product of each weight with its corresponding dimension
    public Double updateSinglePrediction(ArrayList<Double> dataPoint) {
        // contains product of each weight with its corresponding dimension
        ArrayList<Double> productOfWeightNDimension = new ArrayList<>();
        // variable that stores sum of above mention product
        double product = 0;
        // calculate and store product of each weight with its corresponding dimension
        for (int index = 0; index < dataPoint.size() - 1; index++) {
            productOfWeightNDimension.add((singleWeights.get(index) * dataPoint.get(index)));
        }
        // sum all the products
        for (int index = 0; index < productOfWeightNDimension.size(); index++) {
            product += productOfWeightNDimension.get(index);
        }
        // return the product
        return product;
    }

    // method to calculate centroids of category
    public ArrayList<double[]> calculateCenteroids(ArrayList<ArrayList<ArrayList<Double>>> categories) {
        //array that holds centeroids of each category
        ArrayList<double[]> centroids = new ArrayList<>();
        // loop to find centeroid of each category
        for (int category = 0; category < categories.size(); category++) {
            // array to hold centeroid of each category
            double[] centroid = new double[64];
            // nested loop to calculate centeroids of each category
            for (int dimension = 0; dimension < categories.get(category).size() - 1; dimension++) {
                double sumOfCoordinate = 0;
                int numOfCoordinates = 0;
                // loop that iterates through each category to find centroid
                for (int coordinate = 0; coordinate < categories.get(category).get(dimension).size(); coordinate++) {
                    sumOfCoordinate += categories.get(category).get(dimension).get(coordinate);
                    numOfCoordinates++;
                }
                //calculate centroid and store
                centroid[dimension] = sumOfCoordinate / (double) numOfCoordinates;
            }
            centroids.add(centroid);
        }
        return centroids;
    }

    // method that adds 10 more dimensions to each row of data based on distance from centroids of each category
    public void castToHigherDimension(boolean trainingSet) {
        // array that holds extra dimensions
        ArrayList<ArrayList<Double>> extraDimensions = new ArrayList<>();
        // add empty arraylists to avoid out of bound execption
        for (int dimension = 0; dimension < 10; dimension++) {
            extraDimensions.add(new ArrayList<Double>());
        }
        // if trainingSet is true then add dimensions to training set data
        if (trainingSet) {
            // loop to iterate over all the data in dataset
            for (int dataPoint = 0; dataPoint < trainPoints.get(0).size(); dataPoint++) {
                // loop to get row of data from data set
                ArrayList<Double> rowOfData = new ArrayList<>();
                for (int dimension = 0; dimension < trainPoints.size() - 1; dimension++) {
                    rowOfData.add(trainPoints.get(dimension).get(dataPoint));
                }
                // add the extra dimension to each row based on distance from centroid of each category
                for (int centeriod = 0; centeriod < centeroidTrain.size(); centeriod++) {
                    extraDimensions.get(centeriod).add(findDistance(rowOfData, centeroidTrain.get(centeriod)));
                }
            }
            // temporally hold actual value column of dataset
            ArrayList<Double> yValue = trainPoints.remove(trainPoints.size() - 1);
            //loop to add extra dimension columns to dataset
            for (int index = 0; index < extraDimensions.size(); index++) {
                trainPoints.add(extraDimensions.get(index));
            }
            // put the column of actual answers back at the end
            trainPoints.add(yValue);
        }
        // if trainingSet is true then add dimensions to training set data
        if (!trainingSet) {
            // loop to iterate over all the data in dataset
            for (int dataPoint = 0; dataPoint < testPoints.get(0).size(); dataPoint++) {

                // loop to get row of data from data set
                ArrayList<Double> rowOfData = new ArrayList<>();
                for (int dimension = 0; dimension < testPoints.size() - 1; dimension++) {
                    rowOfData.add(testPoints.get(dimension).get(dataPoint));
                }
                // add the extra dimension to each row based on distance from centroid of each category
                for (int centeriod = 0; centeriod < centeriodsTest.size(); centeriod++) {
                    extraDimensions.get(centeriod).add(findDistance(rowOfData, centeriodsTest.get(centeriod)));
                }
            }
            // temporally hold actual value column of dataset
            ArrayList<Double> yValue = testPoints.remove(testPoints.size() - 1);
            //loop to add extra dimension columns to dataset
            for (int index = 0; index < extraDimensions.size(); index++) {
                testPoints.add(extraDimensions.get(index));
            }
            // put the column of actual answers back at the end
            testPoints.add(yValue);
        }
    }

    //method to find distance between datapoint and centroid
    public double findDistance(ArrayList<Double> dataPoint, double[] centeroid) {
        double distance = 0;
        for (int coordinate = 0; coordinate < dataPoint.size(); coordinate++) {
            distance += Math.pow((dataPoint.get(coordinate) - centeroid[coordinate]), 2);
        }
        return (Math.sqrt(distance));
    }

    // this method fills weights array list with zeros
    public ArrayList<Double> fillZero(int size) {
        ArrayList<Double> arrayTobeFilledWithZeros = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            arrayTobeFilledWithZeros.add(0.00);
        }
        return arrayTobeFilledWithZeros;
    }

    // this method loads the data to test and training set arraylists
    public ArrayList<ArrayList<Double>> fileReader(String file, int dimensionConsidered) {

        // arraylist of data that will be returned by method
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        try {
            File aFile = new File(file);
            Scanner scanner = new Scanner(aFile);
            // count iterations
            int countIterations = 0;
            while (scanner.hasNext()) {
                String aLine = scanner.nextLine();
                //split each line at commas
                String[] digit = aLine.split(",");
                //to avoid the null pointer exception execute the code in if block first
                //if it is the first iteration
                if (countIterations == 0) {
                    // loop over each piece in the split line
                    for (int index = 0; index < digit.length - 1; index++) {
                        //initialize new array to hold dimension temporarily
                        ArrayList<Double> tempDimension = new ArrayList<>();
                        //add data to array
                        tempDimension.add(Double.parseDouble(digit[index]));
                        // add the array to arraylist to be returned
                        data.add(tempDimension);
                    }
                    // array list to temporarily hold the actual answers
                    ArrayList<Double> tempDimension = new ArrayList<>();
                    // convert the answer to 1 if the actual answer is equal to the category SVM
                    // will predict
                    if (Double.parseDouble(digit[digit.length - 1]) == dimensionConsidered) {
                        tempDimension.add(1.00);
                        data.add(tempDimension);
                    } else {
                        // change the actual answer of the rest of the values to -1
                        tempDimension.add(-1.00);
                        data.add(tempDimension);
                    }
                    //this code is same as above but it is for iteration more then one to avoid null pointer exception
                } else {
                    // loop over each piece in the split line
                    for (int index = 0; index < digit.length - 1; index++) {
                        data.get(index).add(Double.parseDouble(digit[index]));
                    }
                    // convert the answer to 1 if the actual answer is equal to the category SVM
                    // will predict
                    if (Double.parseDouble(digit[digit.length - 1]) == dimensionConsidered) {
                        data.get(digit.length - 1).add(1.00);
                    } else {
                        // change the actual answer of the rest of the values to -1
                        data.get(digit.length - 1).add(-1.00);
                    }
                }
                countIterations++;
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        // return the data
        return data;
    }

    // method to read data from file and sperate it into categories
    public ArrayList<ArrayList<ArrayList<Double>>> fileReader(String file, ArrayList<ArrayList<ArrayList<Double>>> categories) {

        // nested loop to initialize and store empty array lists to categories
        for (int category = 0; category < 10; category++) {
            ArrayList<ArrayList<Double>> tempDimensions = new ArrayList<>();
            for (int dimension = 0; dimension < 65; dimension++) {
                tempDimensions.add(new ArrayList<Double>());
            }
            categories.add(tempDimensions);
        }
        try {
            File aFile = new File(file);
            Scanner scanner = new Scanner(aFile);
            //int count = 0;
            while (scanner.hasNext()) {
                String aLine = scanner.nextLine();
                //split data at commas
                String[] digit = aLine.split(",");
                double[] categoriesNames = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
                // iterate through data and save data related to each category to its respective array
                for (int category = 0; category < categoriesNames.length; category++) {
                    if (Double.parseDouble(digit[digit.length - 1]) == categoriesNames[category]) {
                        ArrayList<Double> categoryRow = new ArrayList<>();
                        for (int innerIndex = 0; innerIndex < digit.length; innerIndex++) {
                            categoryRow.add(Double.parseDouble(digit[innerIndex]));
                            categories.get((int) categoriesNames[category]).get(innerIndex).add(Double.parseDouble(digit[innerIndex]));
                        }
                    }
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return categories;
    }

    // update weights using following equation
    //weights = for each weight(w-alpha*(2*1/epochs * w)
    public ArrayList<Double> updateWForGreaterThenOne(double currentEpoch) {
        ArrayList<Double> updatedWeights = new ArrayList<>();
        for (int index = 0; index < singleWeights.size(); index++) {
            updatedWeights.add((singleWeights.get(index) - alpha * singleWeights.get(index) * (2 / currentEpoch)));
        }
        return updatedWeights;
    }

    // update weights using following equation
    //weights = for each weight(Wn + alpha*(Dimension-n-value * target-value - 2*1/epochs*Wn))
    public ArrayList<Double> updateWForLessThenOne(double currentEpoch, ArrayList<Double> dataPoint) {
        ArrayList<Double> updatedWeights = new ArrayList<>();
        for (int index = 0; index < singleWeights.size(); index++) {
            updatedWeights.add((singleWeights.get(index) + alpha * (dataPoint.get(index) * dataPoint.get(dataPoint.size() - 1) - (2 / currentEpoch) * singleWeights.get(index))));
        }
        return updatedWeights;
    }

    // method to calculated distance from origin this is used as extra dimension
    public ArrayList<ArrayList<Double>> distanceFromCenter(ArrayList<ArrayList<Double>> data, boolean test) {
        // column of weights containing distance to origin
        ArrayList<Double> distanceToCenterAdded = new ArrayList<>();
        // loop over each row of data
        for (int index = 0; index < data.get(0).size(); index++) {
            //array list to store array of data
            ArrayList<Double> dataRow = rowOfData(index, test);
            // holds distance from centroid
            double distanceFromCenter = 0;
            // loop to calculate distance of each dimension from origin and add it to distanceFromCenter
            for (int dimension = 0; dimension < 64; dimension++) {
                distanceFromCenter += dataRow.get(dimension);
            }
            // add the calculated distance to distanceToCenterAdded array
            distanceToCenterAdded.add(distanceFromCenter);
        }
        // array to temporarily hold actual answers column
        ArrayList<Double> actualAnswer = (data.get(data.size() - 1));
        // remove actual answers column from data set
        data.remove(data.size() - 1);
        // add the extra dimension to data set
        data.add(distanceToCenterAdded);
        // add back the actual answers column to dataset
        data.add(actualAnswer);
        // return updated dataset
        return data;
    }
}
