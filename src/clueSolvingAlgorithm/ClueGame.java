package clueSolvingAlgorithm;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Takes input from user or a file to interact with a Clue.Clue object,
 * in order to simulate a game of clue
 * @author Miles Todtfeld
 */
public class ClueGame {
    /** instance of Clue.Clue to hold and handle clue data and logic */
    private final Clue clue;
    /** number of players */
    private final int n;
    /** which player's turn it is */
    private int turn = 0;
    /** which round it is */
    private int round = 1;
    /** which players are out of the game because they made an incorrect accusation */
    private final boolean[] outPlayers;
    /** reads input of player moves */
    private Scanner in = new Scanner(System.in);
    /** false if reading from a file, true otherwise */
    private boolean manualInput = true;
    /** writes player moves to a file */
    private PrintStream out;
    /** whether or not to print the primitive table from the clue object */
    private boolean printPrimitive = false;
    /** maps two letter abbreviations to the corresponding card number.
     * only works if you're using the same characters, weapons, and rooms as I am. */
    private final HashMap<String, Integer> stringToCard;
    /** The number of rounds we assume that players are not suggesting cards they have themselves.
     * If you're playing with normal people, they usually don't suggest cards that they have, so you can set this value higher,
     * but if you're playing with very smart clue players, set it to 0 or maybe 1.
     * Using this is risky because you could end up assuming wrong and having wrong information. */
    private int numAssumptions = 0;

    public ClueGame(int n, int ... handSizes) {
        this.n = n;
        clue = new Clue(n, handSizes);
        outPlayers = new boolean[n];
        setOutputDestination("data/moves.txt");
        stringToCard = new HashMap<>() {{put("gr", 0);put("mu", 1);put("or", 2);put("pe", 3);put("pl", 4);put("sc", 5);
            put("ca", 6);put("da", 7);put("le", 8);put("re", 9);put("ro", 10);put("wr", 11);put("ba", 12);put("bi", 13);
            put("co", 14);put("di", 15);put("ha", 16);put("ki", 17);put("li", 18);put("lo", 19);put("st", 20);
        }};
    }
    public ClueGame() {
        this(6, 3, 3, 3, 3, 3, 3);
    }

    public void setTimeLimit(long timeLimit) {
        clue.setTimeLimit(timeLimit);
    }
    public void setNumAssumptions(int numAssumptions) {
        this.numAssumptions = numAssumptions;
    }
    public void setInputSource(File file) {
        try {
            in = new Scanner(file);
            manualInput = false;
        } catch(FileNotFoundException ignore) {
            System.out.println("INPUT FILE NOT FOUND");
        }
    }
    public void setInputSource(String filename) {
        try {
            in = new Scanner(new File(filename));
            manualInput = false;
        } catch(FileNotFoundException ignore) {
            System.out.println("INPUT FILE NOT FOUND");
        }
    }
    public void setOutputDestination(String filePath) {
        try {
            out = new PrintStream(new FileOutputStream(filePath, false));
        } catch (IOException ignore) {
            System.out.println("OUTPUT FILE NOT FOUND");
        }
    }

    public void setPrintPrimitive(boolean printPrimitive) {
        this.printPrimitive = printPrimitive;
    }


    /** changes turn and round to the next one */
    private void next() {
        turn = (turn + 1) % n;
        if(turn == 0) round ++;
        if(outPlayers[turn]) next();
    }
    /** makes a move */
    private void turn(int suspect, int weapon, int room, int numTries) {
        clue.addInfo(turn, suspect, weapon, room, numTries);
        if(round <= numAssumptions) {
            clue.setX(turn, suspect);
            clue.setX(turn, weapon);
            clue.setX(turn, room);
        }
    }
    /** makes a move when you know the card handed over */
    private void turn(int suspect, int weapon, int room, int numTries, int cardHandedOver) {
        clue.addInfo(turn, suspect, weapon, room, numTries, cardHandedOver);
    }
    /** when a player makes an incorrect accusation */
    private void accuse(int player, int weapon, int suspect, int room) {
        outPlayers[player] = true;
        clue.incorrectAccusation(player, suspect, weapon, room);
    }

    private int stringToCard(String str) {
        return stringToCard.containsKey(str)? stringToCard.get(str) : Integer.parseInt(str);
    }

    /**
     * Runs the game loop, taking input, making moves, and printing the table.
     * First it will prompt you about your player number, and what cards you have.
     * For example, you would say 0 0 1 2 if you are player 0 and have cards 0, 1, and 2.
     * Then, every turn it will show you the table, and whose turn it is, and it will prompt you about the move they made.
     * If they make a suggestion, write the 3 cards they suggested, and the number of tries it took for someone to have the card.
     * For example if 2 people said they didn't have the cards and the third did, put 3. If nobody had them, put -1.
     * When it's your turn, you know what card they handed over, so add at the end of your input which card they handed over.
     * For example 0 6 12 3 0 means you suggested 0 6 12, the first two people didn't have it, and the third had card 0.
     * If someone makes an incorrect accusation, then while it's their turn, just type the 3 cards they accused.
     * To skip someone's turn, just press enter. If it ever glitches and doesn't work, press delete then enter again.
     */
    public void play() {
        //find out what cards the user has
        System.out.println("Player# cardsInHand...");
        String input = in.nextLine();
        out.print(input);
        System.out.println("<" + input + ">\n\n\n");
        String[] playersCards = input.split(" ");
        for(int i = 1; i < playersCards.length; i ++) {
            clue.setCheck(Integer.parseInt(playersCards[0]), stringToCard(playersCards[i]));
        }
        print(printPrimitive);

        //game loop
        while(manualInput || in.hasNextLine()) {
            System.out.println("\nregularTurn ( suspect weapon room numTries [cardHandedOver] )");
            System.out.println("failedAccusation ( suspect weapon room )");
            input = in.nextLine();
            out.print("\n" + input);
            System.out.println("<" + input + ">\n\n\n\n");
            String[] splitInput = input.split(" ");
            if(input.equals("quit")) break;
            if(splitInput.length == 3) {
                accuse(turn, stringToCard(splitInput[0]), stringToCard(splitInput[1]), stringToCard(splitInput[2]));
            } else if (splitInput.length == 4) {
                turn(stringToCard(splitInput[0]), stringToCard(splitInput[1]), stringToCard(splitInput[2]), Integer.parseInt(splitInput[3]));
            } else if (splitInput.length == 5) {
                turn(stringToCard(splitInput[0]), stringToCard(splitInput[1]), stringToCard(splitInput[2]), Integer.parseInt(splitInput[3]), stringToCard(splitInput[4]));
            }
            next();
            print(printPrimitive);
        }

        if(printPrimitive) clue.printDifference();

    }


    public void print(boolean printPrimitive) {
        System.out.println("--CLUE INFO--");
        System.out.println("isValid = " + clue.isValid());
        if(printPrimitive) {
            System.out.println("-PRIMITIVE TABLE-");
            clue.printPrimitiveTable();
        }
        System.out.println("-LOGIC-");
        clue.printLogic();
        System.out.println("-TABLE-");
        clue.printTable();
        System.out.println("(Round " + round + ")");
        System.out.println("It is now player " + turn + "'s turn");
    }

}
