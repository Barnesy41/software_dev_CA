import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Scanner;

//TODO: Check in main game if cards are being added/removed from deck output file

//TODO: Even thought it is impossible for toString() to be run with an invalid deck, should I still run tests for it?


public class CardDeckTest {
    
    //Functions used for tests 

    //TODO: Add doc strings
    public CardDeck createStandardDeck() {
        CardDeck deck = new CardDeck(1);
        deck.pushTail(new Card(1));
        deck.writeLineToOutputFile("1");
        deck.pushTail(new Card(2));
        deck.writeLineToOutputFile("2");
        deck.pushTail(new Card(3));
        deck.writeLineToOutputFile("3");
        deck.pushTail(new Card(4));
        deck.writeLineToOutputFile("4");

        return deck;
    }

    public String getDeckStringFromFile(Integer deckNumber) {
        try {
            File file = new File("deck" + deckNumber + "_output.txt");

            Scanner reader = new Scanner(file);

            // Read each line, and append it to the list.
            String deckString = "";
            while (reader.hasNextLine()) {
                deckString += " " + reader.nextLine();
            }

            reader.close();
            
            return "deck" + deckNumber +
                " contents:" + deckString;
        }
        catch (Exception e){
            return null;
        }
    }

    //Tests initilization (and that createStandardDeck works)
    @Test
    public void CheckFileIsCreated() {
        createStandardDeck();
        File file = new File("deck1_output.txt");

        assertTrue(file.exists());
    }

    //Checks that the function GetDeckStringFromFile() works 
    @Test
    public void CheckGetDeckStringFromFileWorks() {
        createStandardDeck();
        assertEquals("deck1 contents: 1 2 3 4", getDeckStringFromFile(1));
    }

    //Tests that deck.toString() has correct output with a standard deck
    @Test
    public void checkToString() {
        CardDeck deck = createStandardDeck();
        assertEquals("deck1 contents: 1 2 3 4", deck.toString());
    }

    //Tests that a file gets overwritten if a deck of the same number is created 
    @Test
    public void CheckFileGetsOverwritten() {
        createStandardDeck(); //creates deck 1 with values 1,2,3,4

        CardDeck deck = new CardDeck(1); //creates deck 1 with values 2,4,6,8
        deck.pushTail(new Card(2));
        deck.writeLineToOutputFile("2");
        deck.pushTail(new Card(4));
        deck.writeLineToOutputFile("4");
        deck.pushTail(new Card(6));
        deck.writeLineToOutputFile("6");
        deck.pushTail(new Card(8));
        deck.writeLineToOutputFile("8");
        
        assertEquals("deck1 contents: 2 4 6 8", getDeckStringFromFile(1));
    }

    @Test
    public void getDeckNumberTest() {
        CardDeck deck = createStandardDeck();

        assertEquals(deck.getDeckNumber(), 1);
    }

    @Test
    public void popHeadTest() {
        CardDeck deck = createStandardDeck();

        Card topCard = deck.popHead();
        assertEquals(1, topCard.getValue());
    }

    @Test
    public void pushTailTest() {
        CardDeck deck = createStandardDeck();
        Card finalCard = new Card(5);

        deck.pushTail(finalCard);
        assertEquals(deck.toString(), "deck1 contents: 1 2 3 4 5");
    }

    @Test
    public void writingLineToOutputFileTest() {
        CardDeck deck = createStandardDeck();
        deck.writeLineToOutputFile("100");

        assertEquals("deck1 contents: 1 2 3 4 100", getDeckStringFromFile(1));
    }
}
