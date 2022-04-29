import java.util.ArrayList;

/**
 * This is a Class to store rules
 */
public class Rule {

    private Itemset left;       // Left = Antecedent
    private Itemset right;      // Left = Consequent
    private double confidence;      //Rule's confidence
    private double support;         //Rule's Support

    //Constructors
    public Rule() {
        left = new Itemset();
        right = new Itemset();
        confidence = 0;
        support = 0;
    }

    /**
     * @param left
     * @param right
     */
    public Rule(Itemset left, Itemset right) {
        this.left = left;
        this.right = right;
        this.confidence = 0;
        this.support = 0;
    }


    //  Getters and Setters
    public Itemset getLeft() {
        return left;
    }

    public void setLeft(Itemset left) {
        this.left = left;
    }

    public Itemset getRight() {
        return right;
    }

    public void setRight(Itemset right) {
        this.right = right;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }


    /**
     * This is a function to calculate support of the rule using the input dataArray (tennisArray in this case)
     *
     * @param dataArray
     */
    public void calculateSupport(ArrayList<String[]> dataArray) {

        double result = 0;

        Itemset leftUnionRight = new Itemset();
        leftUnionRight.addItem(this.getLeft().getItemset());
        leftUnionRight.addItem(this.getRight().getItemset());
        leftUnionRight.calculateSupport(dataArray);
        result = leftUnionRight.getSupport() / (double) dataArray.size();

        result = (double) Math.round(result * 100) / 100;

        this.setSupport(result);
    }

    /**
     * This is a function to calculate confidence of the rule using the input dataArray (tennisArray in this case)
     */
    public void calculateConfidence(ArrayList<String[]> dataArray) {

        double result = 0;

        Itemset leftUnionRight = new Itemset();
        leftUnionRight.addItem(this.getLeft().getItemset());
        leftUnionRight.addItem(this.getRight().getItemset());
        leftUnionRight.calculateSupport(dataArray);
        this.getLeft().calculateSupport(dataArray);
        result = (double) leftUnionRight.getSupport() / (double) this.getLeft().getSupport();

        result = (double) Math.round(result * 100) / 100;


        this.setConfidence(result);

    }

}
