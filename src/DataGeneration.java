import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataGeneration {

    static final String filepath = "src/setlist.txt";

    // Track Number of the set
    static int setID = 0;

    // Row or Group
    final static String isRow = "R";
    final static String isGroup = "G";

    // Legality (11 - Fully legal) (01 - Legal with 1/2 Additions)
    final static String isFullyLegal = "11";
    final static String isPartLegal = "01";

    // Which Type of Data does the number hold
    // - (11 - Sequential Stones / Content) TODO i think the sequence does not matter, sorting will be done anyways
    final static String typeContentDiv = "$";
    // - (01 - Appendable Stones)
    final static String typeAppendDiv = "€";
    // - (10 - The sets that the split makes)
    final static String typeSplitDiv = "?";

    // TODO structure of final line: 0R11$b08b09b10b11€b07b12?211?527

    public static void main(String[] args) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false));
            writer.write("");
            writer.close();

            generateSets();

        } catch (IOException e) {
            System.out.println("An error occurred while clearing the file: " + e.getMessage());
        }
    }

    private static void generateSets() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            ArrayList<String> results = new ArrayList<>();

            int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
            String[] colors = {"b", "s", "r", "y"};

            // TODO rows(blue, black, red, yellow)
            // one per set
            for (int i = 0; i < nums.length; i++) {
                for (int j = 0; j < 4; j++) {
                    String col = colors[j];
                    String addition = setID + isRow + isPartLegal; // base
                    addition += typeContentDiv + col + getNumStr(nums[i]); // content
                    addition += typeAppendDiv + col + getNumStr(getPrevNum(nums[i])) + col + getNumStr(getNextNum(nums[i])); // append
                    addition += typeSplitDiv + "-";
                    results.add(addition);
                    tickID();
                }

            }
            // two per set
            // three per set
            // four per set
            // five
            // six
            // seven
            // eight
            // nine
            // ten
            // eleven
            // twelve
            // thirteen
            // TODO groups(1-13)
            // TODO run through results - exchange each of the pieces for one joker
            // TODO run through results - exchange second non-joker with joker

            for (String res: results) {
                writer.write(res + System.lineSeparator());
                tickID();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static String getNumStr (int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    private static int getPrevNum (int number) {
        number--;
        return number <= 0 ? 13 : number;
    }

    private static int getNextNum (int number) {
        number++;
        return number > 13 ? 1 : number;
    }

    private static void tickID() {
        setID++;
    }

}