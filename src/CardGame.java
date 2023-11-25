import java.util.ArrayList;
import java.util.Scanner; // allows user input
public class CardGame {
    public static void main(String[] args) {
        Boolean isWon = false;
        
        int numPlayers = inputNumPlayers();
        String packPath = inputPackPath();

        // Instantiate the pack object
        Pack pack = new Pack(packPath);

        // Ensure that the given pack is valid,
        // Otherwise get a valid pack
        while(!pack.validatePack(numPlayers)){
            packPath = inputPackPath();
            pack = new Pack(packPath);
        }

        // Create the necessary number Player & CardDeck objects, and store them in an array
        //TODO: The preferred value should probably be chosen from the pack, rather than starting from 1 and going up
        ArrayList<Player> playerArray = new ArrayList<Player>();
        ArrayList<CardDeck> cardDeckArray = new ArrayList<CardDeck>();

        //Generates all the decks
        for(int i=0; i<numPlayers; i++) {
            cardDeckArray.add(new CardDeck(i));
        }

        //Generates all players and assigns decks to them
        for(int i=0; i<numPlayers; i++) {
            //determines which deck is assigned as player's discard deck
            int discardDeckNum;
            if (i==(numPlayers-1)) { //number of discard deck wraps round if final player
                discardDeckNum=0;
            }
            else {
                discardDeckNum=i+1;
            }

            playerArray.add(new Player(i+1, cardDeckArray.get(i), cardDeckArray.get(discardDeckNum)));
            playerArray.get(i).start(); //This starts running the thread for the player
        }

        // Deal the cards
        dealCards(pack, playerArray, cardDeckArray);

        // Start the game play cycle
        int numTurns = 0;
        Player winner=null;
        while(!isWon) {
            // Stores which players turn it is
            int playersTurn = numTurns++ % numPlayers;
            Player player = playerArray.get(playersTurn);

            // Syncronised for thread safety
            synchronized (playerArray.get(playersTurn)){
                player.pickupCard();
                player.discardCard();
            }

            if(player.checkWin()){
                isWon = true;
                winner = player;
            }
        }
        System.out.println("player " + winner.getPlayerNum() + " won!\n");
        

    }

    private static int inputNumPlayers(){
        // init scanner
        Scanner scanner = new Scanner(System.in);

        // TODO: check what the minimum numPlayers is
        // Continue asking for inputs until the number of players is valid
        int numPlayers = 0;
        while(numPlayers < 1) {
            try {
                // get the user to input the number of players
                System.out.println("Please Enter The Number Of Players:\n");
                numPlayers = scanner.nextInt();
            }
            catch (Exception e){
                System.out.println("Invalid number of players.\n");
            }
        }

        return numPlayers;
    }

    private static String inputPackPath(){
        // init scanner
        Scanner scanner = new Scanner(System.in);

        // Continue asking for inputs until the path is valid
        String packPath = "";
        while(packPath.equals("")) {
            try {
                // get the user to input the number of players
                System.out.println("Please Enter The Path To The Pack To Load:\n");
                packPath = scanner.nextLine();
            }
            catch (Exception e) {
                System.out.println("Invalid Pack Path.\n");
            }
        }
        return packPath;
    }

    /**
     * dealCards creates card objects for each value in the pack, then deals out the cards to the player & the decks
     *
     * @param pack the pack to deal cards from
     */
    private static void dealCards(Pack pack, ArrayList<Player> playerArray, ArrayList<CardDeck> cardDeckArray){
        ArrayList<Integer> packAsInts = pack.toIntArray();

        //deal cards to players
        int numPlayers = playerArray.size();
        int cardIndex;
        for(cardIndex=0; cardIndex<numPlayers*4; cardIndex++){
            Card card = new Card(packAsInts.get(cardIndex));
            Player playerToDealTo = playerArray.get(cardIndex % numPlayers);
            playerToDealTo.appendToCurrentHand(card);
        }

        // deal cards to CardDecks
        int numCardDecks = numPlayers;
        for(cardIndex=cardIndex;cardIndex<packAsInts.size(); cardIndex++){
            Card card = new Card(packAsInts.get(cardIndex));
            CardDeck deckToDealTo = cardDeckArray.get(cardIndex % numCardDecks);
            deckToDealTo.pushTail(card);
        }
    }
}
