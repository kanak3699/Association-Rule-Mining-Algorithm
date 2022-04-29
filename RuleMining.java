import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RuleMining {

    static String DATA_FILE_NAME = "Play_Tennis_Data_Set.csv";  // Data File Name
    static String OUTPUT_FILE_NAME = "Rules.txt";               // The name of output file

    //ArrayList to store the input data
    static ArrayList<String[]> tennisArray = new ArrayList<String[]>();

    // ArrayLists to store itemsets (candidate and frequent itemsets)
    static ArrayList<ArrayList<Itemset>> C = new ArrayList<>();
    static ArrayList<ArrayList<Itemset>> L = new ArrayList<>();

    static String[] header;             // Header for the file
    static double min_sup, min_conf;    // Minimum Support and Minimum Confidence

    static int k = 0;       // Initializing variable k

    /**
     * This is the main method of the class.
     */
    public static void main(String[] args) throws IOException {

        // Getting Input from input file
        File file = new File(DATA_FILE_NAME);
        Scanner fileScanner = new Scanner(file);
        header = fileScanner.nextLine().split(",");

        while (fileScanner.hasNext()) {
            tennisArray.add(fileScanner.nextLine().split(",")); // splits all the "," from csv file
        }
        fileScanner.close();    // Closing fileScanner

        // Getting User input
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter minimum support: ");
        min_sup = scan.nextDouble();    // Minimum Support in Double
        System.out.print("Enter minimum confidence: ");
        min_conf = scan.nextDouble();   // Minimum Confidence in Double


        // Initializing the first list of candidate itemsets
        if (tennisArray.size() > 0) {
            C.add(new ArrayList<>());
            ++k;
            generateFirstItemset();
        }


        // Looping to create Frequent Itemsets and Candidates from those itemsets until the candidate Itemset becomes empty
        while (!C.get(k - 1).isEmpty() && L.size() < k) {
            calculateLFromC();
            generateNextCandidates();
        }

        //  Most frequent itemsets
        ArrayList<Itemset> lastItemsets = L.get(k - 2);

        // Getting non-empty subsets of lastItemsets
        ArrayList<Itemset> nonEmptySubsets = new ArrayList<>();

        for (Itemset itemset : lastItemsets) {
            if (itemset.getSize() == 2) {
                for (int i = 0; i < itemset.getSize(); i++) {
                    Itemset currentSubset = new Itemset();
                    currentSubset.addItemtoItemset(itemset.getItemset().get(i));
                    boolean alreadyExists = false;

                    for (Itemset nonEmptySubset : nonEmptySubsets) {
                        if (nonEmptySubset.isEqual(currentSubset)) {
                            alreadyExists = true;
                        }
                    }

                    if (!alreadyExists) {
                        nonEmptySubsets.add(currentSubset);
                    }

                }
            }
        }

// Initializing rules

        ArrayList<Rule> rules = new ArrayList<>();
        for (int i = 0; i < nonEmptySubsets.size(); i++) {
            for (int j = i + 1; j < nonEmptySubsets.size(); j++) {

                // Adding rules with Antecedent(Left) and Consequent(Right)
                Itemset set1 = nonEmptySubsets.get(i);
                Itemset set2 = nonEmptySubsets.get(j);

                Rule rule = new Rule(set1, set2);
                rule.calculateConfidence(tennisArray);
                rule.calculateSupport(tennisArray);
                rules.add(rule);

                // Adding rules with Antecedent(Left) and Consequent(Right) in inverse order
                Rule ruleInverse = new Rule(set2, set1);
                ruleInverse.calculateConfidence(tennisArray);
                ruleInverse.calculateSupport(tennisArray);
                rules.add(ruleInverse);

            }
        }

        //Filtering strong rules from all generated rules
        ArrayList<Rule> strongRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.getConfidence() >= min_conf && rule.getSupport() >= min_sup) {
                strongRules.add(rule);
            }
        }

        //StringBuilder to build String output and store in file OUTPUT_FILE_NAME (Rules.txt)
        StringBuilder finaloutput = new StringBuilder();
        finaloutput.append("1. User Input:\n\nSupport=" + min_sup + "\nConfidence=" + min_conf);


        finaloutput.append("\n\n2. Rules:");


        for (int i = 0; i < strongRules.size(); i++) {
            finaloutput.append("\n\nRule#" + (i + 1) + ": ");
            finaloutput.append(printRule(strongRules.get(i)));
        }

        // Writing the output of the program as Rules.txt.
        FileWriter writer = new FileWriter(OUTPUT_FILE_NAME);
        writer.write(finaloutput.toString());
        writer.flush();
        writer.close();

        System.out.println("The output is saved as Rules.txt");
    }


    /**
     * This function generates the first Candidate Itemset.
     *
     * @param
     * @return
     */
    public static void generateFirstItemset() {
        for (int i = 0; i < tennisArray.size(); i++) {
            for (int j = 0; j < tennisArray.get(i).length; j++) {
                ArrayList<String> item = new ArrayList<>();
                item.add(tennisArray.get(i)[j]);

                if (!itemsetExists(item, C.get(0))) {
                    Itemset itemset = new Itemset(item);
                    itemset.calculateSupport(tennisArray);
                    C.get(0).add(itemset);
                }
            }
        }
    }


    /**
     * This is a Boolean function to check if an itemset already exists.
     *
     * @param stringArray - Takes an ArrayList of string array.
     * @param itemsets    - Takes an Arraylist of Itemset.
     * @return true or false
     */
    public static boolean itemsetExists(ArrayList<String> stringArray, ArrayList<Itemset> itemsets) {
        for (Itemset tempset : itemsets) {
            if (tempset.getItemset().equals(stringArray)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function creates L (frequent itemsets) from C (Candidate itemsets)
     *
     * @param
     * @return
     */
    public static void calculateLFromC() {
        if (L.size() < k) {
            L.add(new ArrayList<>());
        }
        int minimumSupport = (int) Math.round(min_sup * tennisArray.size());
        for (Itemset itemset : C.get(k - 1)) {
            if (itemset.getSupport() >= minimumSupport) {
                L.get(k - 1).add(itemset);
            }
        }
    }

    /**
     * This function is to create new Candidate itemsets.
     *
     * @param
     * @return
     */
    //
    public static void generateNextCandidates() {
        int commonItems = k - 1;
        ArrayList<Itemset> previousFrequentItemsets = L.get(k - 1);
        ArrayList<Itemset> newCandidates = new ArrayList<>();

        if (commonItems == 0) {
            for (int i = 0; i < previousFrequentItemsets.size(); i++) {

                for (int j = i + 1; j < previousFrequentItemsets.size(); j++) {
                    Itemset itemset = new Itemset();
                    itemset.addItem(previousFrequentItemsets.get(i).getItemset());
                    itemset.addItem(previousFrequentItemsets.get(j).getItemset());
                    itemset.calculateSupport(tennisArray);
                    if (!itemsetArrayContains(newCandidates, itemset)) {
                        newCandidates.add(itemset);
                    }
                }
            }
        } else {

            for (int i = 0; i < previousFrequentItemsets.size(); i++) {

                for (int j = i + 1; j < previousFrequentItemsets.size(); j++) {

                    Itemset items1 = previousFrequentItemsets.get(i);
                    Itemset items2 = previousFrequentItemsets.get(j);

                    if (items1.canJoin(items2, commonItems)) {
                        Itemset itemset = new Itemset();
                        itemset.addItem(items1.getItemset());
                        itemset.addItem(items2.getItemset());
                        itemset.calculateSupport(tennisArray);
                        if (!itemsetArrayContains(newCandidates, itemset) && isFrequent(itemset)) {
                            newCandidates.add(itemset);
                        }
                    }

                }

            }

        }
        C.add(newCandidates);

        // Increase k by 1
        ++k;

    }

    /**
     * This function is to create Subsets (WIP)
     *
     * @param itemset
     * @return result
     */

    public static ArrayList<Itemset> getSubsets(Itemset itemset) {
        ArrayList<Itemset> result = new ArrayList<>();
        int itemsetSize = itemset.getSize();
        ArrayList<String> items = itemset.getItemset();
        ArrayList<ArrayList<String>> subsetResult = new ArrayList<>();


        backtrack(items, 0, subsetResult, new ArrayList<>(), itemsetSize - 1);
        for (ArrayList<String> subset : subsetResult) {
            result.add(new Itemset(subset));
        }
        return result;
    }

    /**
     * This method is a recursive function to help create subsets in getSubsets function.
     *
     * @param items
     * @param list
     * @param curr
     * @param size
     * @param start
     */
    static void backtrack(ArrayList<String> items, int start, ArrayList<ArrayList<String>> list, ArrayList<String> curr, int size) {


        if (curr.size() == size) {
            list.add(new ArrayList<>(curr));
        }

        for (int i = start; i < items.size(); i++) {
            curr.add(items.get(i));
            backtrack(items, i + 1, list, curr, size);
            curr.remove(curr.size() - 1);
        }


    }

    /**
     * This function is to check if an itemset is frequent by checking if it's subsets are frequent.
     *
     * @param itemset
     * @return false or true
     */

    public static boolean isFrequent(Itemset itemset) {
        ArrayList<Itemset> subsets = getSubsets(itemset);
        ArrayList<Itemset> previousSelectedCandidates = L.get(k - 1);


        for (Itemset subset : subsets) {
            boolean subsetExists = false;
            for (Itemset compItemset : previousSelectedCandidates) {
                if (compItemset.isEqual(subset)) {
                    subsetExists = true;
                }
            }
            if (!subsetExists) {
                return false;
            }
        }

        return false;
    }


    /**
     * This function to get header of an item.
     *
     * @param s - String
     * @return ""
     */
    //
    public static String getHeader(String s) {

        for (int i = 0; i < tennisArray.size(); i++) {
            for (int j = 0; j < tennisArray.get(i).length; j++) {

                if (tennisArray.get(i)[j].equals(s)) {
                    return header[j];
                }
            }
        }
        return "";
    }

    /**
     * This is a Boolean function to check if an Itemset Array contains an itemset object (made to simplify code)
     *
     * @param itemset
     * @param itemsetArray
     * @return true or false
     */
    public static boolean itemsetArrayContains(ArrayList<Itemset> itemsetArray, Itemset itemset) {
        for (Itemset compItemset : itemsetArray) {
            if (compItemset.isEqual(itemset)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is a function to return a String in the desired format for the output
     */

    public static String printRule(Rule rule) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        for (int i = 0; i < rule.getLeft().getSize(); i++) {
            String leftSet = rule.getLeft().getItemset().get(i);
            if (i == rule.getLeft().getSize() - 1) {
                stringBuilder.append(getHeader(leftSet) + "=" + leftSet);
            } else {
                stringBuilder.append(getHeader(leftSet) + "=" + leftSet + ",");
            }
        }

        stringBuilder.append("} => {");

        for (int i = 0; i < rule.getRight().getSize(); i++) {
            String rightSet = rule.getRight().getItemset().get(i);
            if (i == rule.getRight().getSize() - 1) {
                stringBuilder.append(getHeader(rightSet) + "=" + rightSet);
            } else {
                stringBuilder.append(getHeader(rightSet) + "=" + rightSet + ",");
            }
        }

        stringBuilder.append("}\n(Support=" + rule.getSupport() + ", Confidence=" + rule.getConfidence() + ")");

        return stringBuilder.toString();
    }

}
