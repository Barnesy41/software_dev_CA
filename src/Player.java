import java.util.ArrayList;
import java.util.Random;

public class Player extends Thread {
    private final int playerNum;
    private ArrayList<Card> currentHand = new ArrayList<Card>();
    private int numTurnsHad=0;
    private final CardDeck discardDeck;
    private final CardDeck pickupDeck;

    public Player (int playerNum, CardDeck pickupDeck, CardDeck disCardDeck){
        this.playerNum = playerNum;
        this.discardDeck=disCardDeck;
        this.pickupDeck=pickupDeck;
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

    public synchronized void pickupCard() {

        Card newCard = pickupDeck.popHead();
        this.appendToCurrentHand(newCard);
        System.out.println("Player " + (playerNum) + " draws a " + newCard.getValue() + " from deck " + pickupDeck.getDeckNumber());
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
}
