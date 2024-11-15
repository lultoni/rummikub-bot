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
            for (String color : colors) {
                for (int i = 0; i < nums.length; i++) {
                    for (int length = 1; length <= 14 - nums[i]; length++) {
                        String legality = (length >= 3) ? isFullyLegal : isPartLegal;
                        StringBuilder addition = new StringBuilder();
                        addition.append(setID).append(isRow).append(legality);
                        addition.append(typeContentDiv);
                        for (int k = 0; k < length; k++) {
                            addition.append(color).append(getNumStr(nums[(i + k) % nums.length]));
                        }
                        // Appendable stones
                        addition.append(typeAppendDiv);
                        if (length == 12) {
                            // Only the remaining single stone not included
                            for (int n : nums) {
                                boolean isInSet = false;
                                for (int k = 0; k < length; k++) {
                                    if (nums[(i + k) % nums.length] == n) {
                                        isInSet = true;
                                        break;
                                    }
                                }
                                if (!isInSet) {
                                    addition.append(color).append(getNumStr(n));
                                    break;
                                }
                            }
                        } else if (length == 13) {
                            // No stone can be appended
                            addition.append("-");
                        } else {
                            addition.append(color).append(getNumStr(getPrevNum(nums[i])));
                            addition.append(color).append(getNumStr(getNextNum(nums[(i + length - 1) % nums.length])));
                        }
                        addition.append(typeSplitDiv).append("-");

                        results.add(addition.toString());
                        tickID();
                    }
                }
            }
            // TODO groups(1-13)
            for (int num : nums) {
                for (int length = 1; length <= 4; length++) {
                    String legality = (length >= 3) ? isFullyLegal : isPartLegal;
                    StringBuilder addition = new StringBuilder();
                    addition.append(setID).append(isGroup).append(legality);
                    addition.append(typeContentDiv);
                    for (int k = 0; k < length; k++) {
                        addition.append(colors[k]).append(getNumStr(num));
                    }
                    // Appendable stones
                    addition.append(typeAppendDiv);
                    if (length == 4) {
                        // No stone can be appended
                        addition.append("-");
                    } else {
                        for (int k = length; k < colors.length; k++) {
                            addition.append(colors[k]).append(getNumStr(num));
                        }
                    }
                    addition.append(typeSplitDiv).append("-");

                    results.add(addition.toString());
                    tickID();
                }
            }
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