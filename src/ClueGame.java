import java.io.*;
import java.util.Scanner;

/**
 * Takes input from user or a file to interact with a Clue object,
 * in order to simulate a game of clue
 * @author Miles Todtfeld
 */
public class ClueGame {
    /** instance of Clue to hold and handle clue data and logic */
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
    /** writes player moves to a file */
    private PrintStream out;
    /** whether or not to print the primitive table from the clue object */
    private boolean printPrimitive = false;

    public ClueGame(int n, int ... handSizes) {
        this.n = n;
        clue = new Clue(n, handSizes);
        outPlayers = new boolean[n];
        setOutputDestination("data/moves.txt");
    }
    public ClueGame() {
        this(6, 3, 3, 3, 3, 3, 3);
    }

    public void setTimeLimit(long timeLimit) {
        clue.setTimeLimit(timeLimit);
    }
    public void setInputSource(File file) {
        try {
            in = new Scanner(file);
        } catch(FileNotFoundException ignore) {
            System.out.println("INPUT FILE NOT FOUND");
        }
    }
    public void setInputSource(String filename) {
        try {
            in = new Scanner(new File(filename));
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
    private void turn(int weapon, int suspect, int room, int numTries) {
        clue.addInfo(turn, weapon, suspect, room, numTries);
    }
    /** makes a move when you know the card handed over */
    private void turn(int weapon, int suspect, int room, int numTries, int cardHandedOver) {
        clue.addInfo(turn, weapon, suspect, room, numTries, cardHandedOver);
    }
    /** when a player makes an incorrect accusation */
    private void accuse(int player, int weapon, int suspect, int room) {
        outPlayers[player] = true;
        clue.incorrectAccusation(suspect, weapon, room);
    }

    /** runs the game loop, taking input, making moves, and printing the table */
    public void play() {
        //find out what cards the user has
        System.out.println("Player# cardsInHand...");
        String input = in.nextLine();
        out.print(input);
        System.out.println("<" + input + ">\n\n\n");
        String[] playersCards = input.split(" ");
        for(int i = 1; i < playersCards.length; i ++) {
            clue.setCheck(Integer.parseInt(playersCards[0]), Integer.parseInt(playersCards[i]));
        }
        print(printPrimitive);

        //game loop
        while(in.hasNextLine()) {
            System.out.println("\nsuspect weapon room numTries [cardHandedOver]");
            input = in.nextLine();
            out.print("\n" + input);
            System.out.println("<" + input + ">\n\n\n\n");
            String[] splitInput = input.split(" ");
            if (splitInput.length <= 1) break;
            if(splitInput.length == 3) {
                accuse(turn, Integer.parseInt(splitInput[0]),
                        Integer.parseInt(splitInput[1]),
                        Integer.parseInt(splitInput[2]));
            } else if (splitInput.length == 4) {
                turn(
                        Integer.parseInt(splitInput[0]),
                        Integer.parseInt(splitInput[1]),
                        Integer.parseInt(splitInput[2]),
                        Integer.parseInt(splitInput[3])
                );
            } else if (splitInput.length == 5) {
                turn(
                        Integer.parseInt(splitInput[0]),
                        Integer.parseInt(splitInput[1]),
                        Integer.parseInt(splitInput[2]),
                        Integer.parseInt(splitInput[3]),
                        Integer.parseInt(splitInput[4])
                );
            }
            next();
            print(printPrimitive);
        }

        if(printPrimitive) clue.printDifference();

    }


    public void print(boolean printPrimitive) {
        if(printPrimitive) clue.printPrimitiveTable();
        System.out.println("-LOGIC-");
        clue.printLogic();
        System.out.println("-TABLE-");
        clue.printTable();
        System.out.println("(Round " + round + ")");
        System.out.println("It is now player " + turn + "'s turn");
    }

}
