import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataGeneration {

    static final String filepath = "src/setlist.txt";

    // Track Number of the set
    static int setID = 0b0;

    // Row or Group
    final static byte isRow = 0b0;
    final static byte isGroup = 0b1;

    // Legality (11 - Fully legal) (01 - Legal with 1/2 Additions)
    final static byte isFullyLegal = 0b11;
    final static byte isPartLegal = 0b01;

    // Which Type of Data does the number hold
    // - (11 - Sequential Stones / Content)
    final static byte typeContent = 0b11;
    // - (01 - Appendable Stones and the Side)
    final static byte typeAppendable = 0b01;
    // - (10 - Splits and the sets that the split makes)
    final static byte typeSplits = 0b10;

    public static void main (String[] args) {
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
                String result = generateSet();
                writer.write(Integer.toBinaryString(setID) + "_" + result + System.lineSeparator());
                tickID();

                if (setID >= 1000) {
                    unfinished = false;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static String generateSet() {
        // For demonstration, let's assume a set contains a combination of isRow/isGroup, isFullyLegal/isPartLegal, etc.
        // Customize based on actual set requirements
        return Integer.toBinaryString(isRow) + Integer.toBinaryString(isFullyLegal) + Integer.toBinaryString(typeContent);
    }

    private static void tickID() {
        setID++;
    }
}
