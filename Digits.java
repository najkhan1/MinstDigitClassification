package digitrecognition;

/**
 * class that creates digit objects for nearest neighbours
 * classification
 */
public class Digits {
    // array of dimensions of each digit
    private int[] dimensions;
    // actual answer of each row of data
    private int value;

    //parametrised constructor of the class
    public Digits(int[] dimensions, int value) {
        this.dimensions = dimensions;
        this.value = value;
    }

    // default constructor
    public Digits() {
    }
    // method to access the dimensions of the object
    public int[] getDimensions() {
        return dimensions;
    }
    // method to set the dimensions of the object
    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }
    // method to get the actual answer
    public int getValue() {
        return value;
    }
    // method to set the actual answer
    public void setValue(int value) {
        this.value = value;
    }
}
