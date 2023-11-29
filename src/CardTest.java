import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CardTest {

    //Tests that getValue() works as expected
    @Test
    public void getValueTest() {
        Card testCard = new Card(4);
        assertEquals(4, testCard.getValue());
    }
}
