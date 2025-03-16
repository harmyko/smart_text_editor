import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BookFilter {
    public static void main (String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java BookFilter <input file> \"<author>\"");
            return;
        }

        String filename = args[0];
        String author = args[1];

        printBookNames(filename, author);
        
    }

    /**
     * Reads a file containing book descriptions and prints the titles of books
     * written by the specified author.
     *
     * @param filename the name of the file containing book descriptions
     * @param author the name of the author whose books are to be printed
     * 
     * Expected format of the file:
     * <author>|<title>|<year>
     */
    public static void printBookNames (String filename, String author) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int matchCount = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3 && parts[0].trim().equalsIgnoreCase(author.trim())) {
                    matchCount++;
                    System.out.println(matchCount + ". Title: " + parts[1].trim());
                }
            }

            if (matchCount == 0) {
                System.out.println("There are no books written by " + author + " in " + filename + "!");
            }

        } catch (IOException e) {
            System.out.println("An error occured while trying to read " + filename + ".");
        }
    }
}