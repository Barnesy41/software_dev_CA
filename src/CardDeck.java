import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CardDeck {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private int deckNumber;
    private final File outputFile;
    private final String outputFilePath;

    /**
     * instantiates the CardDeck object: initialises its attributes & creates/clears its output file.
     *
     * @param deckNumber the identifier for the deck
     */
    public CardDeck(int deckNumber) {
        this.deckNumber=deckNumber;
        this.outputFilePath = "deck" + deckNumber + "_output.txt";
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
     * @return the identifier for the deck
     */
    public int getDeckNumber() {
        return deckNumber;
    }

    /**
     * The popHead method returns and removes the top card from the deck
     *
     * @return head - the top card from the deck
     */
    public synchronized Card popHead() {
        Card head = deck.get(0);
        deck.remove(0);
        return head;
    }

    /**
     * the pushTail methods appends a card to the bottom of the deck
     *
     * @param cardObject the card to place on the bottom of the deck
     */
    public synchronized void pushTail(Card cardObject){
        deck.add(cardObject);
    }

    /**
     * Returns the contents of a deck as a string
     * i.e.
     * deck1 contents: 1 2 3 4
     */
    @Override
    public String toString() {
        String cardValuesString = "";
        for(Card card : deck) cardValuesString += " " + card.getValue();

        return "deck" + deckNumber +
                " contents:" + cardValuesString;
    }

    /**
     * Writes a line to the CardDeck's output file
     *
     * @param string the string to write to the text file
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
