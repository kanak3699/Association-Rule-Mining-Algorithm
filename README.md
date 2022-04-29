# Association-Rule-Mining-Algorithm

The program uses the Apriori algorithm to generate frequent itemsets and thereafter provide all strong association rules

## Apriori Algorithm

The Apriori Algorithm is used to generate frequent itemsets. Once the frequent itemsets are available, the association rules can be easily generated.

## Sample Data Set

A sample data set file titled “Play_Tennis_Data_Set.csv” is used as the data
set for this assignment. Specifically:

a) CSV stands for Comma-Separated Values. A CSV file is a text file that uses a comma to
separate values. Often, the first record in a CSV file is a header line including a list of field
names. Therefore, it is very easy to dig into a CSV file and look for useful information. You
can use any text editor to open a CSV file and view its content. More details about CSV
can be found here: https://en.wikipedia.org/wiki/Comma-separated_values
b) The sample data set is available with the code. There are 14 records (not including the
header line) in the data set. The data set includes 5 fields: Outlook, Temperature,
Humidity, Windy, and PlayTennis. Please note that these fields are equally important in
terms of association rule mining. Namely, PlayTennis is not a special field; both
“{Humidity=normal} => {PlayTennis=P}” and “{PlayTennis=P} => {Humidity=normal}” are
possible association rules.


## Instructions to run on local computer

1) Create a folder called src and add RuleMining.java, Rule.java, Itemset.java files into that folder.
2) Keep the "Play_Tennis_Data_Set.csv" outside of the folder.
3) Open the program and run it. (I have used IntelliJ Software)

## Program Specification

After RuleMining is executed via the
command-based interface, RuleMining should prompt the user to enter the minimum
support threshold and the minimum confidence threshold, which are used to generate the
frequent itemsets and association rules.

a. The user input for min_sup and min_conf should be fraction values, such as 0.25.

b. Note that the Apriori algorithm uses the minimum support count to generate
frequent itemsets. In this assignment, the minimum support count should be
obtained by multiplying min_sup by the total number of tuples in the data set, and
thereafter rounding the product up to the closest integer. For example, with the
provided sample data set, when min_sup=0.25, the minimum support count is
equal to [0.25 x 14] = [3.5] = 4.

c) With the provided minimum support and minimum confidence, your program will read
“Play_Tennis_Data_Set.csv” and generate all strong association rules. The resulting
association rules will be saved in a file named “Rules.txt”, which should be placed in the
directory where your program file is located.

d) Appendix 1 at the end of this file illustrates the appropriate structure of “Rules.txt”. Please
note:
  a. The order of the generated rules does not matter. As long as the set of rules are
correct, you will not lose marks.
  b. The support and confidence of each generated rule should be rounded to the
nearest hundredth. For example, if the original value of the support is 0.256, it
should be rounded to 0.26. If the original value of the support is 0.264, it should
be rounded to 0.26 too.
