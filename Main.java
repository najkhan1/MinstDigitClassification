package digitrecognition;
/**
 * this is the main class that runs the whole program by calling classify method of Classifier two class
 *
 */
public class Main {

    //indicates which test fold is running
    public static int fold = 1;
    // value of hyperparameter alpha
    public static double ALPHA = 0.0002;
    // value of hyperparameter number of epochs
    public static double EPOCHS = 40;
    // paths of test and training datasets
    public static String FILEPATHTRAIN = "D:\\yearIIINew\\ArtificialIntelligence\\CourseWorkII\\cw2DataSet1.csv";
    public static String FILEPATHTEST  = "D:\\yearIIINew\\ArtificialIntelligence\\CourseWorkII\\cw2DataSet2.csv";

    public static void main(String[] args) {

        //declare and initialize object that trains all the SVMs and the classifies test data
        Classifier svm = new Classifier();
        // method that trains SVM on training data and classifies test data
        svm.classify();

    }
}
