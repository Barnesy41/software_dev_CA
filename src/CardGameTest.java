import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
    public void negativeNumberOfPlayers() throws FileNotFoundException {
            //Sets player input (numPlayers, packPath)
            ByteArrayInputStream in = new ByteArrayInputStream("-4\n4\n4players.txt".getBytes());
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

    //Tests the expected output when 0 is output for number of players (game should prompt for new input)
    @Test
    public void numberOPlayersLowerBoundary() throws FileNotFoundException {
         //Sets player input (numPlayers, packPath)
         ByteArrayInputStream in = new ByteArrayInputStream("0\n4\n4players.txt".getBytes());
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

    //Tests when 1 is inputted as number of players (game should run as normal)
    @Test
    public void numberOPlayersUpperBoundary() throws FileNotFoundException {
        //Sets player input (numPlayers, packPath)
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1players.txt".getBytes());
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

        File file = new File("player1_output.txt");

        Scanner reader = new Scanner(file);

        // Read each line, and append it to the list.
        String playerOutput = "";
        while (reader.hasNextLine()) {
            playerOutput += "\n" + reader.nextLine();
        }

        reader.close();

        //check that the player wins
        assertTrue(playerOutput.contains("player 1 wins"));
   }

    //tests that the game prompts for another input when player enters a string for the number of players
    @Test
    public void stringAsNumberOfPlayers() throws FileNotFoundException {
            //Sets player input (numPlayers, packPath)
            ByteArrayInputStream in = new ByteArrayInputStream("string\n4\n4players.txt".getBytes());
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

    //tests that the game prompts for another input when player enters a pack file that doesn't exist
    @Test
    public void invalidPackPath() throws FileNotFoundException {
            //Sets player input (numPlayers, packPath)
            ByteArrayInputStream in = new ByteArrayInputStream("4\ninvalidFileLocation.txt\n4players.txt".getBytes());
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

    //tests that the player is prompted to re-input the pack path when they choose a pack that isn't compatible with the number of players
    @Test
    public void invalidPackForNumberOfPlayers() throws FileNotFoundException {
        //Sets player input (numPlayers, packPath)
        ByteArrayInputStream in = new ByteArrayInputStream("3\n4players.txt\n3players.txt".getBytes());
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

        for (int i = 1; i <= 3; i++) { //checks each player output
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

        for (int i = 0; i <= 2; i++) { //checks each player output contains the correct reference to winner
            if (i != winnerPlayerNum - 1) { //this line means all threads get stopped
                assertTrue(playerOutputs.get(i).contains("player " + winnerPlayerNum + " has informed player " + (i + 1) + " that player " + winnerPlayerNum + " has won"));
            }
        }
    }

    //tests that when the pack deals a player a winning hand, they always win
    @Test
    public void winningHandFromStart() throws FileNotFoundException {
        for (int j = 0; j<10; j++) { //runs 10 times to ensure player 1 always wins
             //Sets player input (numPlayers, packPath)
            ByteArrayInputStream in = new ByteArrayInputStream("32\n32players.txt".getBytes());
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


        //Gets the output of the game from each player
        ArrayList<String> playerOutputs = new ArrayList<>();

        int winnerPlayerNum = 1;

            //Gets the output of the game from each player
            for (int i = 1; i <= 32; i++) { //checks each player output
                File file = new File("player" + i + "_output.txt");

                Scanner reader = new Scanner(file);

                // Read each line, and append it to the list.
                String playerOutput = "";
                Integer lineCounter = 0;
                while (reader.hasNextLine()) {
                    playerOutput += "\n" + reader.nextLine();
                    lineCounter++;
                }

                //checks if the current player is a winner (if they have a complete hand)
                if (playerOutput.contains("player " + i + " wins")) {
                    winnerPlayerNum = i;
                }
                else { //if not winner, each file should have 4 lines
                    assertEquals(Integer.valueOf(4), lineCounter);
                }

                reader.close();
                playerOutputs.add(playerOutput);
            }

            assertEquals(32, winnerPlayerNum); //player 1 should always win
        }
    }
}
