import java.util.ArrayList;

public class CardDeck {
    private ArrayList<Card> deck = new ArrayList<Card>();

    /**
     * The popHead method returns and removes the top card from the deck
     *
     * @return head - the top card from the deck
     */
    public Card popHead() {
        Card head = deck.get(0);
        deck.remove(0);
        return head;
    }

    /**
     * the pushTail methods appends a card to the bottom of the deck
     *
     * @param cardObject the card to place on the bottom of the deck
     */
    public void pushTail(Card cardObject){
        deck.add(cardObject);
    }
}
