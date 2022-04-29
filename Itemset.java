import java.util.ArrayList;

// Class to store itemsets
public class Itemset {
    private ArrayList<String> itemset; //String Array to store string itemsets
    private int support;    //support of itemset

    //constructors
    public Itemset() {
        this.itemset = new ArrayList<>();
        this.support = 0;
    }

    public Itemset(ArrayList<String> itemset) {
        this.itemset = itemset;
    }

    public Itemset(ArrayList<String> itemset, int support) {
        this.itemset = itemset;
        this.support = 0;
    }

    // getters and setters
    public ArrayList<String> getItemset() {
        return itemset;
    }

    public void setItemset(ArrayList<String> itemset) {
        this.itemset = itemset;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }


    /**
     * This is a function to add item(s) to the Itemset. (Also checks and skips an item/string that is already present in this itemset).
     *
     * @param newItemset
     */
    public void addItem(ArrayList<String> newItemset) {

        if (this.itemset.isEmpty()) {
            this.itemset.addAll(newItemset);
        } else {
            for (String newItem : newItemset) {
                if (!itemExists(newItem)) {
                    this.itemset.add(newItem);
                }
            }
        }

    }

    /**
     * This function adds String directly to this itemset.
     *
     * @param s
     */
    public void addItemtoItemset(String s) {
        this.itemset.add(s);
    }

    //

    /**
     * This is a function to calculate support of the itemset with the help of input dataArray (tennisArray in this case).
     *
     * @param dataArray
     */
    public void calculateSupport(ArrayList<String[]> dataArray) {

        int itemsetSize = this.itemset.size();

        int support = 0;

        for (String[] row : dataArray) {
            int kFlag = 0;
            for (String item : row) {
                if (kFlag <= itemsetSize && this.itemset.contains(item)) {
                    ++kFlag;
                    if (kFlag == itemsetSize) {
                        ++support;
                        kFlag = 0;
                    }
                }
            }
        }
        this.setSupport(support);
    }

    /**
     * This is a Boolean function to check if an item/String exists in the itemset.
     *
     * @param s
     * @return true or false
     */
    public boolean itemExists(String s) {

        for (String item : this.itemset) {
            if (item.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is a Boolean function to check if the passed itemset is equal to this itemset.
     *
     * @param itemset
     * @return true or false
     */

    public boolean isEqual(Itemset itemset) {
        int flag = itemset.getItemset().size();
        for (String item : itemset.getItemset()) {
            if (itemExists(item)) {
                --flag;
            }
        }
        return flag < 1;
    }

    /**
     * This is a function to get the size of the itemset
     */
    public int getSize() {
        return this.itemset.size();
    }

    /**
     * This is a Boolean function to validate if two itemsets can be joined for Apriori algorithm
     *
     * @param itemset
     * @param commonItems
     * @return true or false
     */
    public boolean canJoin(Itemset itemset, int commonItems) {

        if (commonItems < 1) {
            return true;
        }

        ArrayList<String> itemsetArray = itemset.getItemset();
        ArrayList<String> compItemsetArray = this.getItemset();

        for (String compItemsetString : compItemsetArray) {
            for (String itemsetString : itemsetArray) {
                if (itemsetString.equals(compItemsetString)) {
                    --commonItems;
                }
            }
        }

        return commonItems == 0;
    }

}
