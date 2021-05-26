package digitrecognition;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * class that trains and tests SVMs of categories 0-9
 */
public class Classifier {
    //target holds data to be tested
    ArrayList<ArrayList<Double>> target = fileReader(Main.FILEPATHTEST);
    // number of no prediction that will be passed to nearest neighbour for classification
    int noPredictions = 0;
    //number of correct answers
    int correctAnswers = 0;
    //number of total predictions made
    int totalPredictions = 0;
    // object that preforms nearest neighbour classification
    DigitRecognizerEuclidean euclideanDistance = new DigitRecognizerEuclidean();
    /**
     * this method trains all 10 SVM each for one category using a ONE VS ALL
     * approach. this method then classifies all the test data using a decision
     * tree approach.
     */
    public void classify() {
        // Arraylist that holds references to SVMs of each category
        // train method trains SVMs of all categories and return Arraylist of trained SVMs
        ArrayList<SVM> trainedSVMs = train();
        // method to classify the test set takes array of trained SVMs as input
        makeClassification(trainedSVMs);

    }

    //method to train all 10 categories of SVMs
    public ArrayList<SVM> train(){
        ArrayList<SVM> svmCategories = new ArrayList<>();
        //loop to initialize SVM of each category
        for (int index = 0; index < 10; index++) {
            //declaring and initializing each SVM by passing it category
            //it needs to classify
            SVM svm = new SVM(index);
            svmCategories.add(svm);
        }
        // loop to train each svm on a separate thread
        for (int index = 0; index < 10; index++) {
            //start training each SVM on a separate thread
            svmCategories.get(index).start();
        }
        //loop to collate the trained SVM when training finishes
        for (int index = 0; index < 10; index++) {
            try {
                //wait until the training is finished and end the thread
                svmCategories.get(index).join();
            } catch (InterruptedException error) {
                error.printStackTrace();
            }
        }
        return svmCategories;
    }

    // method to classify the test set take arraylist of trained SVMs as parameter
    public void makeClassification(ArrayList<SVM> trainedSVMs){
        // loop that loops through the decision tree looking for a positive
        // classification when it is found it jumps to next iteration
        // there is no positive prediction through out all the SVMs
        // it is then classified by nearest neighbour algorithm
        for (int index = 0; index < target.size(); index++) {
            // decision tree that iterates through each SVM looking
            //for a positive classification if it happens the loop jumps to
            // next iteration immediately
            if (trainedSVMs.get(0).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(1).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(2).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(3).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(4).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(5).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(6).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(7).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(8).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else if (trainedSVMs.get(9).predict(index) == 1) {
                correctAnswers++;
                totalPredictions++;
                continue;
            } else {
                noPredictions++;
                // if no SVM provides a positive classification then that row
                // of data is passed to nearest neighbour algorithm for classification
                if (euclideanDistance.findDigit(target.get(index)) == 1) {
                    correctAnswers++;
                    totalPredictions++;
                }
            }

        }
        System.out.println("Accuracy " + ((double) correctAnswers / (double) target.size()) * 100);
        System.out.println("prediction by nearest neighbour " + noPredictions);
        System.out.println("total predictions " + totalPredictions);
        System.out.println("correct Answers " + correctAnswers);
        System.out.println(" test Fold " + Main.fold);
    }

    /**
     * method to parse the data from file
     *
     * @param file path of dataset
     * @return data as arraylist of arraylists
     */
    public ArrayList<ArrayList<Double>> fileReader(String file) {

        // holds reference to data loaded from dataset
        ArrayList<ArrayList<Double>> target = new ArrayList<>();
        try {
            File aFile = new File(file);
            Scanner scanner = new Scanner(aFile);
            while (scanner.hasNext()) {
                String nextLine = scanner.nextLine();
                // split the data at comma and store in array
                String[] digit = nextLine.split(",");
                // row of data that will be added to target
                ArrayList<Double> dataPoint = new ArrayList<>();
                // loop to store data dataPoint array
                for (int index = 0; index < digit.length; index++) {
                    dataPoint.add(Double.parseDouble(digit[index]));
                }
                // add dataPoint to arraylist that will be returned by the method
                target.add(dataPoint);

            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        // return the data
        return target;
    }


}


