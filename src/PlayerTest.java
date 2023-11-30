import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

public class PlayerTest {

    //The run() method is tested in the CardGameTest

    //Creates and returns a standard player with pickup and discard decks
    private Player createStandardPlayer() {

        //Generates decks for the player
        CardDeck pickupDeck = new CardDeck(1);
        CardDeck disCardDeck = new CardDeck(2);

        pickupDeck.pushTail(new Card(1));
        pickupDeck.pushTail(new Card(2));
        pickupDeck.pushTail(new Card(3));
        pickupDeck.pushTail(new Card(4));

        disCardDeck.pushTail(new Card(5));
        disCardDeck.pushTail(new Card(6));
        disCardDeck.pushTail(new Card(7));
        disCardDeck.pushTail(new Card(8));

        //Returns player with these decks
        return (new Player(1, pickupDeck, disCardDeck));
    }

    //Tests using getCurrentHand() on a player with an empty hand
    @Test
    public void getEmptyHandTest() {
        Player player = createStandardPlayer();

        assertEquals(new ArrayList<Card>(), player.getCurrentHand());
    }

    //Tests that getCurrentHand() works on a hand full of cards
    @Test
    public void getBusyHandTest() {
        Player player = createStandardPlayer();
        
        ArrayList<Card> hand = new ArrayList<Card>();

        //Sets up the hand
        Card card1 = new Card(10);
        Card card2 = new Card(20);

        hand.add(card1);
        hand.add(card2);

        player.appendToCurrentHand(card1);
        player.appendToCurrentHand(card2);

        //Tests that each card in both arrays are equal
        for (int i = 0; i<hand.size(); i++) {
            assertEquals(hand.get(i), player.getCurrentHand().get(i));
        }
    }

    //Tests that currentHandToString() works as expected
    @Test
    public void currentHandToStringTest () {
        Player player = createStandardPlayer();

        player.appendToCurrentHand(new Card(50));
        player.appendToCurrentHand(new Card(100));

        assertEquals(player.currentHandToString(), "50 100 ");
    }

    //Appends to an empty hand
    @Test
    public void appendToEmptyHandTest() {
        Player player = createStandardPlayer();

        player.appendToCurrentHand(new Card(999));
        assertEquals("999 ", player.currentHandToString());
    }

    //Appends to a hand that has multiple cards in it
    @Test
    public void appendToBusyHand() {
        Player player = createStandardPlayer();

        player.appendToCurrentHand(new Card(999));
        player.appendToCurrentHand(new Card(1000));
        player.appendToCurrentHand(new Card(1001));
        assertEquals("999 1000 1001 ", player.currentHandToString());
    }

    //Tests removing a card from a current hand (that is in the current hand)
    @Test
    public void removeCardFromCurrentHand() {
        Player player = createStandardPlayer();
        
        Card selectedCard = new Card(1000);

        //Adds cards to hand
        player.appendToCurrentHand(new Card(999));
        player.appendToCurrentHand(selectedCard);
        player.appendToCurrentHand(new Card(1001));

        //Removes card from hand
        player.removeFromCurrentHand(selectedCard);

        assertEquals("999 1001 ", player.currentHandToString());
    }
    
    //Tests attempting to remove a card from current hand thats not in the hand (hand should stay the same)
    @Test
    public void removeCardNotInCurrent() {
        Player player = createStandardPlayer();

        //Adds cards to hand
        player.appendToCurrentHand(new Card(999));
        player.appendToCurrentHand(new Card(1000));
        player.appendToCurrentHand(new Card(1001));

        //Removes card from hand
        player.removeFromCurrentHand(new Card(10));

        assertEquals("999 1000 1001 ", player.currentHandToString());
    }

    //tests that the player's hand is as expected when using pickupCard()
    @Test
    public void pickupCardTestHand() {
        Player player = createStandardPlayer();

        player.appendToCurrentHand(new Card(999));
        
        player.pickupCard();

        //checks that card from top of pickup deck is added to hand
        assertEquals("999 1 ", player.currentHandToString());
    }

    //tests that a file for a player gets overwritten if a player with the same number is created
    @Test
    public void overwritePlayerFileTest() throws FileNotFoundException {
        String output = "";
        
        //Initial player (which results in 1 line of file output)
        Player player = createStandardPlayer();
        player.pickupCard();

        //Second player (with same number) that should overwrite initial player
        Player newPlayer = createStandardPlayer();
        newPlayer.pickupCard();
        newPlayer.pickupCard();

        //reads from output file
        File file = new File("player1_output.txt");
        Scanner reader = new Scanner(file);

        output += reader.nextLine();
        output += reader.nextLine();
        reader.close();

        //if, and only if, the file gets overwritten, the following output is expected
        assertEquals("player 1 draws a 1 from deck 1player 1 draws a 2 from deck 1", output);
    }

    //tests that the output file displays as expected when using pickupCard()
    @Test
    public void pickupCardTestOutputFile() throws FileNotFoundException {
        Player player = createStandardPlayer();
        String output = "";
        
        //tests picking up multiple cards
        player.pickupCard();
        player.pickupCard();

        //reads output from file
        File file = new File("player1_output.txt");
        Scanner reader = new Scanner(file);

        output += reader.nextLine();
        output += reader.nextLine();

        reader.close();

        //checks that card from top of pickup deck is added to hand
        assertEquals("player 1 draws a 1 from deck 1player 1 draws a 2 from deck 1", output);
    }

    //tests that discardCard() works as expected (discards a random card that isnt the preferred card)
    @Test
    public void discardCardTestHand() {
        
        for (int i = 0; i < 100; i++) { //runs 100 times to ensure that the preferred card is NEVER discarded
            Player player = createStandardPlayer();

            //adds cards to hands (including 1 with preferred value)
            player.appendToCurrentHand(new Card(1));
            player.appendToCurrentHand(new Card(2));
            player.appendToCurrentHand(new Card(3));
            player.appendToCurrentHand(new Card(4));

            player.discardCard();

            //Possible options for hands after discarding card (1 should not be discarded)
            String[] optionsForHand = {"1 2 3 ", "1 3 4 ", "1 2 4 "};

            //checks that the player's hand reflects one of the above options
            assertTrue(Arrays.asList(optionsForHand).contains(player.currentHandToString()));
        }
    }

    //tests that the correct output is printed to file when player discards a card
    @Test
    public void discardCardTestOutputFile() throws FileNotFoundException {

        //only need to run this test once as we assume the non-preffered card has been chosen due to the previous test discardCardTestHand

        Player player = createStandardPlayer();
        String output = "";
        
        //adds cards to hands (including 1 with preferred value)
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(2));
        player.appendToCurrentHand(new Card(3));
        player.appendToCurrentHand(new Card(4));

        player.discardCard();

        //reads output from file
        File file = new File("player1_output.txt");
        Scanner reader = new Scanner(file);

        output += reader.nextLine();
        reader.close();

        //Possible options for output lines after discarding card (1 should not be discarded)
        String[] optionsForFile = {"player 1 discards a 2 to deck 2", "player 1 discards a 3 to deck 2", "player 1 discards a 4 to deck 2"};

        //checks that the player's hand reflects one of the above options
        assertTrue(Arrays.asList(optionsForFile).contains(output));
    }
    
    //tests the checkWin() method when the player has a winning hand
    @Test
    public void checkWinWithWinningHand() {
        Player player = createStandardPlayer();

        //adds cards to hands
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(1));

        assertTrue(player.checkWin());
    }

    //tests the checkWin() method when the player does not have a winning hand
    @Test
    public void checkWinWithNonWinningHand() {
        Player player = createStandardPlayer();

        //adds cards to hands
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(2));

        assertFalse(player.checkWin());
    }

    //tests the getPlayerNum() method
    @Test
    public void getPlayerNumTest() {
        Player player = createStandardPlayer();

        assertEquals(1, player.getPlayerNum());
    }

    //tests that player hand gets correctly written to file
    @Test
    public void writeCurrentHandToOutputFileTest() throws FileNotFoundException {
        Player player = createStandardPlayer();
        String output = "";
        
        //adds cards to hands (including 1 with preferred value)
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(2));
        player.appendToCurrentHand(new Card(3));
        player.appendToCurrentHand(new Card(4));

        player.writeCurrentHandToOutputFile();

        //reads output from file
        File file = new File("player1_output.txt");
        Scanner reader = new Scanner(file);

        output += reader.nextLine();
        reader.close();

        assertEquals("player 1 current hand is 1 2 3 4 ", output);
    }

    //tests that player hand gets correctly written to file
    @Test
    public void writeFinalHandToOutputFileTest() throws FileNotFoundException {
        Player player = createStandardPlayer();
        String output = "";
        
        //adds cards to hands (including 1 with preferred value)
        player.appendToCurrentHand(new Card(1));
        player.appendToCurrentHand(new Card(2));
        player.appendToCurrentHand(new Card(3));
        player.appendToCurrentHand(new Card(4));

        player.writeFinalHandToOutputFile();

        //reads output from file
        File file = new File("player1_output.txt");
        Scanner reader = new Scanner(file);

        output += reader.nextLine();
        reader.close();

        assertEquals("player 1 final hand: 1 2 3 4 ", output);
    }

    //tests that writeLineToOutputFile() works as expected
    @Test
    public void writeLineToOutputFileTest() throws FileNotFoundException {
        Player player = createStandardPlayer();
        String output = "";

        //tests writing multiple lines
        player.writeLineToOutputFile("I am testing that:");
        player.writeLineToOutputFile("player correctly writes line to output file");

        //reads from output file
        File file = new File("player1_output.txt");
        Scanner reader = new Scanner(file);

        output += reader.nextLine();
        output += reader.nextLine();
        reader.close();

        assertEquals("I am testing that:player correctly writes line to output file", output);
    }
}
