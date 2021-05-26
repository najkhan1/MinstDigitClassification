package digitrecognition;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * class to classify data using nearest neighbour algorithm
 */
public class DigitRecognizerEuclidean {

    // training dataset
    ArrayList<Digits> trainDigits = fileReader(Main.FILEPATHTRAIN);
    // test dataset
    ArrayList<Digits> testDigits = fileReader(Main.FILEPATHTEST);

    // method that takes in row of data can classifies it using nearest
    // neighbour algorithm
    public int findDigit(ArrayList<Double> testPoint) {
        Digits pointToBeTested = convertToDigit(testPoint);
        int lowestDistance = Integer.MAX_VALUE;
        Digits digitTobeChecked = new Digits();
        // loop over all training data to find nearest neighbour
        for (Digits trainDigit : trainDigits) {
            if (findDistance(pointToBeTested, trainDigit) < lowestDistance) {
                lowestDistance = findDistance(pointToBeTested, trainDigit);
                digitTobeChecked = trainDigit;

            }
        }
        // if the classified answer and actual answer is equal return 1
        // ie if classification is positive and correct return 1
        if (digitTobeChecked.getValue() == pointToBeTested.getValue()) {
            return 1;
        } else {
            return -1;
        }
    }

    // method to find euclidean distance
    public int findDistance(Digits toBeChecked, Digits toBeCheckedAgainst) {
        int sumOfCoordinates = 0;
        for (int coordinate = 0; coordinate < toBeChecked.getDimensions().length; coordinate++) {
            sumOfCoordinates += Math.pow((toBeChecked.getDimensions()[coordinate] - toBeCheckedAgainst.getDimensions()[coordinate]), 2);
        }
        return sumOfCoordinates;
    }

    // this method converts row of data to Digits object to be processed by f
    // ind digit method
    public Digits convertToDigit(ArrayList<Double> testDataPoint) {
        int[] dimensionsOfNewDigit = new int[testDataPoint.size() - 1];
        for (int index = 0; index < testDataPoint.size() - 1; index++) {
            double eachDimension = testDataPoint.get(index);
            dimensionsOfNewDigit[index] = (int) eachDimension;
        }
        double newDigitValue = testDataPoint.get(testDataPoint.size() - 1);
        Digits newDigit = new Digits(dimensionsOfNewDigit, (int) newDigitValue);
        return newDigit;
    }

    /**
     * method reads data from file
     * @param file path of file in string format
     * @return arraylist of Digits objects
     */
    public ArrayList<Digits> fileReader(String file) {

        ArrayList<Digits> data = new ArrayList<>();

        try {
            File aFile = new File(file);
            Scanner scanner = new Scanner(aFile);
            while (scanner.hasNext()) {
                int[] listOfDigitDimensions = new int[64];
                String aLine = scanner.nextLine();
                // split line at commas
                String[] digit = aLine.split(",");
                // process the read line
                for (int aDigit = 0; aDigit < digit.length - 1; aDigit++) {
                    listOfDigitDimensions[aDigit] = Integer.parseInt(digit[aDigit]);
                }
                // declare and initialize Digits object and assign it the processed data
                Digits digitsObject = new Digits(listOfDigitDimensions, Integer.parseInt(digit[digit.length - 1]));
                // add digits object to arraylist to be returned
                data.add(digitsObject);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        // return the read data
        return data;
    }
}
