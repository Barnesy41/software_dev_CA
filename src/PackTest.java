import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PackTest {

    @Test
    public void isValidPackTestFileExistsCorrectNumPlayers() {
        Pack pack = new Pack("4players.txt");
        assertTrue(pack.isValidPack(4));
    };
};