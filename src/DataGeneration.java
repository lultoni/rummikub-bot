import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
            boolean unfinished = true;
            while (unfinished) {
                String result = generateSet(); // TODO find a way to not gen them one by one but grouped together -> which groups exist in the first
                // TODO rows(blue, black, red, yellow) groups(1-13)
                // TODO !!! per row and group, exchange each of the pieces for one joker
                // TODO !!! !!! per each joker containing group, exchange second non-joker with joker
                if (result != null) {
                    writer.write(result + System.lineSeparator());
                    tickID();
                }

                if (setID >= 1000) {  // Limit set count for demonstration
                    unfinished = false;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static String generateSet() { // TODO generate a single set?
        ArrayList<String> setData = new ArrayList<>();

        // Example attributes for a Row set
        setData.add(formatSetID(setID));
        setData.add(Integer.toBinaryString(isRow));
        setData.add(Integer.toBinaryString(isFullyLegal));
        setData.add(Integer.toBinaryString(typeContent));
        setData.add(generateStoneSequence());

        // Appendable stones
        setData.add(generateAppendableStones());

        // Splittable index data
        setData.add(generateSplitData());

        return String.join("", setData);
    }

    private static String generateStoneSequence() {
        // Assume a simple sequence for demonstration, e.g., "011100" for a row set
        // This should encode stone color and number (e.g., Blue 1, Blue 2, Blue 3)
        return "(011100)"; // TODO check stone list
    }

    private static String generateAppendableStones() {
        // Return binary-encoded data for appendable stones and side (left or right)
        return "(000000)"; // TODO check stone list
    }

    private static String generateSplitData() {
        // Generate split information for this set, if applicable
        return "(00110)(0)(000000)"; // TODO check stone list
    }

    private static void tickID() {
        setID++;
    }
}
