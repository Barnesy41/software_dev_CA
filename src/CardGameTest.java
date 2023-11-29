import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import org.junit.Test;

public class CardGameTest {
    
    //Tests inputNumberPlayers()

    //tests that the game runs and completes successfully when the game is setup successfully
    @Test
    public void acceptableGameSetup() throws FileNotFoundException {
            //Sets player input (numPlayers, packPath)
            ByteArrayInputStream in = new ByteArrayInputStream("4\n4players.txt".getBytes());
            System.setIn(in);

            new CardGame();

            //wait for all player threads to stop before finishing function run
            ArrayList<Thread> threads = CardGame.getThreadArray();
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); //Ensures all threads stay interrupted
                }
                thread.interrupt(); // Ensures the thread is interrupted
            }

            int winnerPlayerNum = 1;

            //Gets the output of the game from each player
            ArrayList<String> playerOutputs = new ArrayList<>();

            for (int i = 1; i <= 4; i++) { //checks each player output
                File file = new File("player" + i + "_output.txt");

                Scanner reader = new Scanner(file);

                // Read each line, and append it to the list.
                String playerOutput = "";
                while (reader.hasNextLine()) {
                    playerOutput += "\n" + reader.nextLine();
                }

                //checks if the current player is a winner (if they have a complete hand)
                if (playerOutput.contains("player " + i + " wins")) {
                    winnerPlayerNum = i;
                }

                playerOutputs.add(playerOutput);
                reader.close();
            }

            for (int i = 0; i <= 3; i++) { //checks each player output contains the correct reference to winner
                if (i != winnerPlayerNum - 1) { //this line means all threads get stopped
                    assertTrue(playerOutputs.get(i).contains("player " + winnerPlayerNum + " has informed player " + (i + 1) + " that player " + winnerPlayerNum + " has won"));
                }
            }
    }

    //tests that the game prompts user for another input if given a negative number of players, before running successfully 
    @Test
    public void negativeNumPlayers() throws FileNotFoundException {
        //Sets player input (numPlayers, packPath)
        ByteArrayInputStream in = new ByteArrayInputStream("-1\n4\n4players.txt".getBytes());
        System.setIn(in);

        new CardGame();

        int winnerPlayerNum = 1;

        //Gets the output of the game from each player
        ArrayList<String> playerOutputs = new ArrayList<>();

        for (int i = 1; i<=4; i++) { //checks each player output
            File file = new File("player" + i + "_output.txt");

            Scanner reader = new Scanner(file);

            // Read each line, and append it to the list.
            String playerOutput = "";
            while (reader.hasNextLine()) {
                playerOutput += "\n" + reader.nextLine();
                System.out.println(playerOutput);
            }

            //checks if the current player is a winner (if they have a complete hand)
            if (playerOutput.contains("current hand is 1 1 1 1") || 
                playerOutput.contains("current hand is 2 2 2 2") || 
                playerOutput.contains("current hand is 3 3 3 3") || 
                playerOutput.contains("current hand is 4 4 4 4")) {
                    winnerPlayerNum = i;
                }
            
            playerOutputs.add(playerOutput);
            reader.close();
        } 

        for (int i = 0; i<=3; i++) { //checks each player output contains the correct reference to winner 
            if (i != winnerPlayerNum-1) { //this line means all threads get stopped
                System.out.println(playerOutputs.get(i));
                System.out.println(winnerPlayerNum);
                assertTrue(playerOutputs.get(i).contains("player " + winnerPlayerNum + " has informed player " + (i+1) + " that player " + winnerPlayerNum + " has won "));
            }
        }
    }
}
