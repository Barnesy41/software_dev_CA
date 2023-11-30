import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Player extends Thread {
    private final int playerNum;
    private ArrayList<Card> currentHand = new ArrayList<Card>(); // is not final, ignore the error
    private final CardDeck discardDeck;
    private final CardDeck pickupDeck;
    private final File outputFile;
    private final String outputFilePath;

    /**
     * instantiates a player object.
     * initialises the player output file
     * initialises class variables
     *
     * @param playerNum the number of players playing the game
     * @param pickupDeck the deck object that the player picks up from the top of
     * @param disCardDeck the deck object that the player discards to the bottom of
     */
    public Player (int playerNum, CardDeck pickupDeck, CardDeck disCardDeck){
        this.playerNum = playerNum;
        this.discardDeck=disCardDeck;
        this.pickupDeck=pickupDeck;
        this.outputFilePath = "player" + playerNum + "_output.txt";
        this.outputFile = new File(outputFilePath);

        // Clear the output file if it already exists
        try {
            PrintWriter writer = new PrintWriter(outputFile);
            writer.print("");
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * runs once its thread has been started.
     * Contains the main gameplay loop for each player, including picking up and putting down cards, as well
     * as the players strategy of doing so.
     * Allows for the game to be multi-threadded.
     */
    public void run(){

        while(!Thread.interrupted()){
            boolean hasWon = this.checkWin();

            // Check if the player has won
            if (hasWon) {
                CardGame.setWin(this);
                currentThread().interrupt();
            }

            // otherwise make the player take their turn
            // Check if the thread has been interrupted again incase a player won
            else if(pickupDeck.getNumCardsInDeck() != 0) {
                this.pickupCard();
                this.discardCard();
                this.writeCurrentHandToOutputFile();
            }
        }
    }

    /**
     * Adds a card to the player's current hand
     *
     * @param card the card object to add to the player's hand
     */
    public void appendToCurrentHand(Card card){
        currentHand.add(card);
    }

    /**
     * removes a given card object from the player's current hand.
     *
     * @param cardObject the card object to remove from the player's hand
     */
    public void removeFromCurrentHand(Card cardObject){
        for (int i=0; i<currentHand.size(); i++){
            if (currentHand.get(i) == cardObject){
                currentHand.remove(i);
                break;
            }
        }
    }

    /**
     * returns the current hand of a player
     *
     * @return the current hand of the player as an ArrayList
     */
    public ArrayList<Card> getCurrentHand(){
        return currentHand;
    }

    /**
     * pickupCard picks up a card from the top of the deck to the left of the player, places it in the players hand,
     * and outputs a string representation of this to the player's output file
     */
    public void pickupCard() {

        Card newCard = pickupDeck.popHead();
        this.appendToCurrentHand(newCard);

        // Write to the output file
        writeLineToOutputFile(
                          "player " + playerNum +
                                " draws a " + newCard.getValue() +
                                " from deck " + pickupDeck.getDeckNumber()
                             );
    }

    /**
     * Discards a card from the player's hand at random. it does this using the java.util.random library
     * The discarded card is then sent to the bottom of the player's discard deck
     *
     */
    public void discardCard() {

        Card cardToDiscard = null;

        //finds a card that can be discarded (so a card that is not a preferred one)
        while(cardToDiscard==null) {
            Random rand = new Random();
            int int_random = rand.nextInt(4);

            Card randomCard = currentHand.get(int_random);
            if (randomCard.getValue() != (playerNum)) { //if not a preferred card, the card can be discarded
                cardToDiscard=randomCard;
            }
        }

        //Send the card to discard to the bottom of the discard deck
        discardDeck.pushTail(cardToDiscard);
        this.removeFromCurrentHand(cardToDiscard); // and discard it from the player's hand

        // Write to the output file
        writeLineToOutputFile(
                        "player " + playerNum +
                              " discards a " + cardToDiscard.getValue() +
                              " to deck " + discardDeck.getDeckNumber()
                            );
    }

    /**
     * Compares the value of all cards in a players hand. If the value of each card is equal, the player has won,
     * Otherwise the player has not won
     *
     * @return true if the player has won, false otherwise.
     */
    public Boolean checkWin(){
        int cardValue = currentHand.get(0).getValue();
        for (int i=1; i<currentHand.size(); i++){
            if (currentHand.get(i).getValue() != cardValue) return false;
        }
        return true;
    }

    /**
     * @return the player's identifier/ their preferred card number
     */
    public int getPlayerNum(){
        return playerNum;
    }

    /**
     * Outputs the players current hand in string format
     * e.g. '1 2 3 4 '
     *
     * @return the player's current hand in string format
     */
    public String currentHandToString(){
        String currentHandAsString = "";
        for(Card card : currentHand){
            currentHandAsString += card.getValue() + " ";
        }

        return currentHandAsString;
    }

    /**
     * writes the player's current hand to their output file in the required format
     */
    public void writeCurrentHandToOutputFile(){
        writeLineToOutputFile(
                          "player " + playerNum +
                                " current hand is " + currentHandToString()
                             );
    }

    /**
     * writes the player's final hand to their output file in the required format
     */
    public void writeFinalHandToOutputFile(){
        writeLineToOutputFile(
                "player " + playerNum +
                        " final hand: " + currentHandToString()
        );
    }

    /**
     * wrotes the line string to the player's output file
     * @param string the string to write
     */
    public void writeLineToOutputFile(String string){
        try {
            FileWriter writer = new FileWriter(outputFilePath, true);
            writer.write(string + "\n"); // Add a new line after each write
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
