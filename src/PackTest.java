import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

public class PackTest {

    //Testing toIntArray method

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

    @Test
    public void toIntArrayFileIsString () {
        Pack pack = new Pack("packIncorrectFormat.txt");
        assertNull(pack.toIntArray());
    }

    //Testing isValidPack method

    @Test
    public void isValidPackTestFileExistsCorrectNumPlayers() {
        Pack pack = new Pack("4players.txt");
        assertTrue(pack.isValidPack(4));
    };

    @Test
    public void isValidPackIncorrectNumPlayers() {
        Pack pack = new Pack("4players.txt");
        assertTrue(pack.isValidPack(3)==false);
    };

    @Test
    public void isValidPackTestFileDoesntExist() {
        Pack pack = new Pack("nonexistentfile.txt");
        assertTrue(pack.isValidPack(4)==false);
    };

    @Test
    public void isValidPackIncorrectFormat() {
        Pack pack = new Pack("packIncorrectFormat.txt");
        assertTrue(pack.isValidPack(4)==false);
    };

    @Test
    public void isValidPackNegativeIntegers() {
        Pack pack = new Pack("negativeIntegersPack.txt");
        assertTrue(pack.isValidPack(4)==false);
    };

};