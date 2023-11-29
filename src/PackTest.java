import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

//TODO: Should I allow filePath to be passed as arguments to test instead of using ones I created?

public class PackTest {

    //Testing toIntArray method

    //Tests that toIntArray() correctly converts pack file to an int array (using standard pack)
    @Test
    public void toIntArrayCorrectlyConverts () {
        Pack pack = new Pack("4players.txt");
        ArrayList<Integer> expectedResult = new ArrayList<>(Arrays.asList(4, 1, 6, 4, 8, 3, 3, 1, 1, 4, 8, 3, 1, 2, 6, 6, 2, 6, 2, 8, 7, 5, 5, 5, 7, 7, 2, 5, 3, 8, 7, 4));
        ArrayList<Integer> actualResult = pack.toIntArray();
        
        //checks every item in the arrays are the same, and checks they are the same length
        assertEquals(expectedResult.size(), actualResult.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i), actualResult.get(i));
        }
    }

    //Tests that null is returned when user attempts to use toIntArray() on file in incorrect format
    @Test
    public void toIntArrayFileIsString () {
        Pack pack = new Pack("packIncorrectFormat.txt");
        assertNull(pack.toIntArray());
    }

    //Testing isValidPack method

    //Tests that isValidPack() returns true when the pack and exists and is accurate for that number of players
    @Test
    public void isValidPackTestFileExistsCorrectNumPlayers() {
        Pack pack = new Pack("4players.txt");
        assertTrue(pack.isValidPack(4));
    };

    //Tests that isValidPack() returns false if the pack is not built for specified number of players
    @Test
    public void isValidPackIncorrectNumPlayers() {
        Pack pack = new Pack("4players.txt");
        assertFalse(pack.isValidPack(3));
    };

    //Tests that isValidPack() returns false if the file specified doesn't exist
    @Test
    public void isValidPackTestFileDoesntExist() {
        Pack pack = new Pack("nonexistentfile.txt");
        assertFalse(pack.isValidPack(4));
    };

    //Tests that isValidPack() returns false if the file is not in the correct format
    @Test
    public void isValidPackIncorrectFormat() {
        Pack pack = new Pack("packIncorrectFormat.txt");
        assertFalse(pack.isValidPack(4));
    };

    //Tests that isValidPack() returns false if the pack contains negative integers
    @Test
    public void isValidPackNegativeIntegers() {
        Pack pack = new Pack("negativeIntegersPack.txt");
        assertFalse(pack.isValidPack(4));
    };

};