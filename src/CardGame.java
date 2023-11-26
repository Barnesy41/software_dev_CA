import java.util.ArrayList;
import java.util.Scanner; // allows user input
import java.util.concurrent.CyclicBarrier;

public class CardGame {
    private static ArrayList<CardDeck> cardDeckArray;
    private static ArrayList<Player> playerArray;
    private final int numPlayers;
    private final Pack pack;
    private static ArrayList<Thread> playerThreadArray;

    public CardGame(){
        playerThreadArray = new ArrayList<Thread>();

        // get required user inputs
        numPlayers = 4;
        String packPath = "4players.txt";

        // Instantiate the pack object
        Pack inputPack = new Pack(packPath);

        // Ensure that the given pack is valid,
        // Otherwise ask for a valid pack
        while(!inputPack.isValidPack(numPlayers)){
            packPath = inputPackPath();
            inputPack = new Pack(packPath);
        }
        pack = inputPack; //TODO: make the above nicer, so pack = function() only.

        // Instantiate arrays to store the players & card decks
        playerArray = new ArrayList<Player>();
        cardDeckArray = new ArrayList<CardDeck>();

        //Generates all the CardDecks
        for(int i=1; i<=numPlayers; i++) {
            cardDeckArray.add(new CardDeck(i));
        }

        //Create a barrier to ensure that all threads wait until all other threads are done before continuing
        CyclicBarrier barrier = new CyclicBarrier(numPlayers);

        //Generate all players
        // Assign CardDecks to players, 1 to discard cards to and 1 to pick cards up from
        for(int i=0; i<numPlayers; i++) {
            //determines which deck is assigned as player's discard deck
            int discardDeckNum;
            if (i==(numPlayers-1)) { //number of discard deck wraps round if final player
                discardDeckNum=0;
            }
            else {
                discardDeckNum=i+1;
            }

            // instantiate each player, and assign them a thread
            Player player = new Player(i+1, cardDeckArray.get(i), cardDeckArray.get(discardDeckNum), barrier);
            Thread thread = new Thread(player);

            // add each player & thread to an array
            playerArray.add(player);
            playerThreadArray.add(thread);
        }

        // Deal the cards
        dealCards(pack, playerArray, cardDeckArray);

        // Start all threads for players
        for (Thread thread : playerThreadArray){
            thread.start();
        }
    }

    /**
     * asks the user to input the number of players playing the card game & validates said input
     *
     * @return the number of players
     */
    private static int inputNumPlayers(){
        // init scanner
        Scanner scanner = new Scanner(System.in);

        // TODO: check what the minimum numPlayers is
        // Continue asking for inputs until the number of players is valid
        int numPlayers = 0;
        while(numPlayers < 1) {
            try {
                // get the user to input the number of players
                System.out.println("Please enter the number of players:");
                numPlayers = scanner.nextInt();
            }
            catch (Exception e){
                System.out.println("Invalid number of players.");
            }
        }

        return numPlayers;
    }

    /**
     * gets the user to input the file location of the pack.
     *
     * @return the file location of the pack
     */
    private static String inputPackPath(){
        // init scanner
        Scanner scanner = new Scanner(System.in);

        // Continue asking for inputs until the pack file location given is valid
        String packPath = "";
        while(packPath.equals("")) {
            try {
                // get the user to input the number of players
                System.out.println("Please enter location of pack to load:");
                packPath = scanner.nextLine();
            }
            catch (Exception e) {
                System.out.println("Invalid Pack Path.");
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

    /**
     * Does the necessary details once a player is confirmed to have won the game
     *
     * stops all player threads from running
     * outputs necessary win details to player output files
     * outputs necessary details to CardDeck output files
     *
     * @param winningPlayerObject the Player object of the winning player
     */
    public static synchronized void setWin(Player winningPlayerObject){
        // ensure that only 1 thread can call this method
        if(!Thread.interrupted()) {
            // Stop all other threads
            interruptAllPlayerThreads();

            // Output necissary info to the terminal
            System.out.println("player " + winningPlayerObject.getPlayerNum() + " wins");
            System.out.println("With the hand: " + winningPlayerObject.currentHandToString());

            // inform other players that the winner has won & all of them exit (output to their files)
            int winnerPlayerNum = winningPlayerObject.getPlayerNum();
            int playerNum;
            for (Player player : playerArray){
                playerNum = player.getPlayerNum();

                // Inform all players if they have won or not
                if (player != winningPlayerObject) {
                    player.writeLineToOutputFile(
                              "player " + winnerPlayerNum +
                                    " has informed player " + playerNum +
                                    " that player " + winnerPlayerNum +
                                    " has won "
                    );
                }
                else player.writeLineToOutputFile("player " + winnerPlayerNum + " wins");

                // Inform all players to exit
                player.writeLineToOutputFile("player " + playerNum + " exits");

                // Output each player's hand to the output file
                player.writeFinalHandToOutputFile();

            }

            // Write to the CardDeck output files
            for (CardDeck deck : cardDeckArray){
                deck.writeLineToOutputFile( deck.toString() );
            }


        }
    }

    /**
     * interrupts the threads of all players, stopping the threads from running.
     */
    public static synchronized void interruptAllPlayerThreads(){
        for(Thread thread : playerThreadArray) thread.interrupt();
    }

    public static void main(String[] args) {
        new CardGame(); // Yes this is needed, despite it not looking like it should be

    }

}
