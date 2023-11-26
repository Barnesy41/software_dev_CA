import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Pack {
    private final String filePath;

    /**
     * Instantiates the Pack object
     *
     * @param filePath the file location of the pack file
     */
    public Pack (String filePath){
        this.filePath = filePath;
    }

    /**
     * The packToIntArray function converts a pack text file into an array of integers.
     *
     * @return packArray returns an ArrayList of ints, each element corresponding to a line in the pack file.
     *                   Returns null if there is an error.
     */
    public ArrayList<Integer> toIntArray(){
        try {
            // Try to read the pack file
            File file = new File(filePath);

            //init a file reader
            Scanner reader = new Scanner(file);

            // Read each line, and append it to the list.
            ArrayList<Integer> packArray = new ArrayList<Integer>();
            while (reader.hasNextLine()) {
                packArray.add(reader.nextInt());
            }

            return packArray;
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * The validatePack file checks whether a file path is to a valid pack file
     *
     * @param numPlayers the number of players playing the game
     *
     * @return true if the pack is valid, & false if the pack is invalid
     */
    public boolean isValidPack(int numPlayers){
        try{
            // Check that no errors occur when reading the file
            if (this.toIntArray() == null) return false;

            // Check that the pack file is the correct number of lines (8n)
            ArrayList<Integer> packArray = new ArrayList<Integer>(this.toIntArray()); // Cannot be null
            if (packArray.size() != 8 * numPlayers) return false;

            // Check that the pack file contains non-negative integers
            for (int value : packArray) if (value < 0) return false;

        }
        // If an error is produced, the pack is invalid.
        catch(Exception e){
            System.out.println("The Given Pack File Could Not Be Read. Please Use A Valid Pack File.\n");
            return false;
        }

        // If pack file was read correctly, return true
        return true;
    }
}
