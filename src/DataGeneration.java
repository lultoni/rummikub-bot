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
    final static String isSingle = "S";

    // Legality (11 - Fully legal) (01 - Legal with 1/2 Additions)
    final static String isFullyLegal = "11";
    final static String isPartLegal = "01";

    // Which Type of Data does the number hold
    // - (11 - Sequential Stones / Content)
    final static String typeContentDiv = "$";
    // - (01 - Appendable Stones)
    final static String typeAppendDiv = "€";
    // - (10 - The sets that the split makes)
    final static String typeSplitDiv = "?";

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

            // rows(blue, black, red, yellow)
            for (String color : colors) {
                for (int i = 0; i < nums.length; i++) {
                    for (int length = 1; length <= 14 - nums[i]; length++) {
                        String legality = (length >= 3) ? isFullyLegal : isPartLegal;
                        StringBuilder addition = new StringBuilder();
                        addition.append(getSetID()).append(length == 1 ? isSingle : isRow).append(legality);
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
                        } else if (length == 1) {
                            addition.append(color).append(getNumStr(getPrevNum(nums[i])));
                            addition.append(color).append(getNumStr(getNextNum(nums[i])));
                            switch (color) {
                                case "b" -> {
                                    addition.append("s").append(getNumStr(nums[i]));
                                    addition.append("r").append(getNumStr(nums[i]));
                                    addition.append("y").append(getNumStr(nums[i]));
                                }
                                case "s" -> {
                                    addition.append("b").append(getNumStr(nums[i]));
                                    addition.append("r").append(getNumStr(nums[i]));
                                    addition.append("y").append(getNumStr(nums[i]));
                                }
                                case "r" -> {
                                    addition.append("s").append(getNumStr(nums[i]));
                                    addition.append("b").append(getNumStr(nums[i]));
                                    addition.append("y").append(getNumStr(nums[i]));
                                }
                                case "y" -> {
                                    addition.append("s").append(getNumStr(nums[i]));
                                    addition.append("r").append(getNumStr(nums[i]));
                                    addition.append("b").append(getNumStr(nums[i]));
                                }
                            }
                        } else {
                            addition.append(color).append(getNumStr(getPrevNum(nums[i])));
                            addition.append(color).append(getNumStr(getNextNum(nums[(i + length - 1) % nums.length])));
                        }
                        addition.append(typeSplitDiv).append(generateSplits(addition));

                        results.add(addition.toString());
                        tickID();
                    }
                }
            }
            // groups(1-13)
            for (int num : nums) {
                for (int length = 2; length <= 4; length++) { // TODO this only give me b01 + whatever but not the other variations (find a way to not make dupes tho)
                    String legality = (length >= 3) ? isFullyLegal : isPartLegal;
                    StringBuilder addition = new StringBuilder();
                    addition.append(getSetID()).append(isGroup).append(legality);
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
                    addition.append(typeSplitDiv).append(generateSplits(addition));

                    results.add(addition.toString());
                    tickID();
                }
            }
            // First Joker
            ArrayList<String> joker_1_Results = new ArrayList<>();
            for (int i = 0;  i < results.size(); i++) {
                if ("S".equals(String.valueOf(results.get(i).charAt(4)))) {
                    // if is Single -> do nothing?
                    // System.out.println(results.get(i).substring(8, 12) + " - " + results.get(i).substring(0, 4));
                } else {
                    StringBuilder addition = new StringBuilder();
                    addition.append(getSetID()).append(result_type).append(result_legality);
                    addition.append(typeContentDiv);
                    // TODO write the content here
                    addition.append(typeAppendDiv);
                    // TODO write appending stones here
                    addition.append(typeSplitDiv).append(generateSplits(addition));

                    joker_1_Results.add(addition.toString());
                    tickID();
                }

            }
            // Second Joker
            ArrayList<String> joker_2_Results = new ArrayList<>();
            // run through joker_1_Results
            // replace a second non-joker with joker

            results.addAll(joker_1_Results);
            results.addAll(joker_2_Results);
            for (String res: results) {
                writer.write(res + System.lineSeparator());
                tickID();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static String generateSplits(StringBuilder addition) {
        // TODO from the addition work out into which sets you can split the set
        // make a list of all the sublist combinations
        // look for those sublist's in the possible sets and then put in that setID
        // TODO how is the structure looking (sid, sid, div, ... || or '-' if no split is possible)
        return "-";
    }

    private static String getSetID() {
        if (setID >= 0 && setID < 10) return "000" + setID;
        if (setID >= 10 && setID < 100) return "00" + setID;
        if (setID >= 100 && setID < 1000) return "0" + setID;
        return String.valueOf(setID);
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