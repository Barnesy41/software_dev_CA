import java.util.ArrayList;

public class Player {
    private int preferredValue;
    private ArrayList<Card> currentHand = new ArrayList<Card>();

    public Player (int preferredValue){
        this.preferredValue = preferredValue;
    }

    public void appendToCurrentHand(Card card){
        currentHand.add(card);
    }

    public void removeFromCurrentHand(int index){
        currentHand.remove(index);
    }

    public ArrayList<Card> getCurrentHand(){
        return currentHand;
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
}
