import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Player extends Thread {
    private final int playerNum;
    private ArrayList<Card> currentHand = new ArrayList<Card>(); // is not final, ignore the error
    private final CardDeck discardDeck;
    private final CardDeck pickupDeck;
    private final CyclicBarrier barrier; // Ensures threads all play the same number of turns by the end of the game
    private final File outputFile;
    private final String outputFilePath;

    public Player (int playerNum, CardDeck pickupDeck, CardDeck disCardDeck, CyclicBarrier barrier){
        this.playerNum = playerNum;
        this.discardDeck=disCardDeck;
        this.pickupDeck=pickupDeck;
        this.barrier = barrier;
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

    public void run(){
        this.writeLineToOutputFile("player " + playerNum +
                                    " initial hand " + currentHandToString()
        );

        while(!Thread.interrupted()){
            boolean hasWon = this.checkWin();

            // Check if the player has won
            if (hasWon) {
                CardGame.setWin(this);
            }
            // otherwise make the player take their turn
            else {
                this.pickupCard();
                this.discardCard();
                this.writeCurrentHandToOutputFile();

            }

            // continue when all other threads have also taken their turns
            // if it throws an error, its because a thread won, so we dont care about it
            //TODO: break; is bad practise, but it works.
            try{
                barrier.await();
            } catch (Exception e){
                break;
            }
        }
    }

    public void appendToCurrentHand(Card card){
        currentHand.add(card);
    }

    public void removeFromCurrentHand(Card cardObject){
        for (int i=0; i<currentHand.size(); i++){
            if (currentHand.get(i) == cardObject){
                currentHand.remove(i);
                break;
            }
        }
    }

    public ArrayList<Card> getCurrentHand(){
        return currentHand;
    }

    /**
     * pickupCard picks up a card from the top of the deck to the left of the player, places it in the players hand,
     * and outputs a string representation of this to the player's output file
     */
    public synchronized void pickupCard() {

        Card newCard = pickupDeck.popHead();
        this.appendToCurrentHand(newCard);

        // Write to the output file
        writeLineToOutputFile(
                          "player " + playerNum +
                                " draws a " + newCard.getValue() +
                                " from deck " + pickupDeck.getDeckNumber()
                             );
    }

    public synchronized void discardCard() {

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

    public int getPlayerNum(){
        return playerNum;
    }

    public String currentHandToString(){
        String currentHandAsString = "";
        for(Card card : currentHand){
            currentHandAsString += card.getValue() + " ";
        }

        return currentHandAsString;
    }

    public void writeCurrentHandToOutputFile(){
        writeLineToOutputFile(
                          "player " + playerNum +
                                " current hand is " + currentHandToString()
                             );
    }

    public void writeFinalHandToOutputFile(){
        writeLineToOutputFile(
                "player " + playerNum +
                        " final hand: " + currentHandToString()
        );
    }

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
