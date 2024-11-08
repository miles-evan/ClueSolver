package clueSolvingAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Handles a clue scorecard table, to figure out the hands of each player
 * @author Miles Todtfeld
 */
public class Hands {
    /** number of players */
    private final int n;
    /**
     * table of checks, Xs, and empty squares.
     * each row is a player, each column is a card.
     * true means that player has that card.
     * false means that player does not have that card.
     * null means we don't know yet
     * the last row is the answer hand
     */
    private final Boolean[][] table;
    private final int[] numChecksInRow;
    private final int[] numXsInRow;
    /** number of Xs in the answer hand for each type (suspect, weapon, room) */
    private final int[] numAnswerTypeXs;
    private final int[] numXsInCol;
    private final boolean[] colHasCheck;
    /** number of cards we know the player has. (the player hand has 3 cards) */
    private final int[] handSizes;
    /** maps the number of cards to their abbreviations */
    private final String[] cardToString = new String[]{"gr","mu","or","pe","pl","sc","ca","da","le","re","ro","wr",
                                                "ba","bi","co","di","ha","ki","li","lo","st"};
    /** Tells you which card have been added to the logic and which have not */
    private HashSet<Integer> cardsInLogic = new HashSet<>();

    public Hands() {
        this(6, 3, 3, 3, 3, 3, 3);
    }
    public Hands(int n, int ... handSizes) {
        this.n = n;
        table = new Boolean[n+1][21];
        numChecksInRow = new int[n+1];
        numXsInRow = new int[n+1];
        this.handSizes = new int[n+1];
        System.arraycopy(handSizes, 0, this.handSizes, 0, n);
        this.handSizes[n] = 3;
        numAnswerTypeXs = new int[3];
        numXsInCol = new int[21];
        colHasCheck = new boolean[21];
    }

    /**copies another hands object*/
    public Hands(Hands other) {
        n = other.n;
        table = new Boolean[n+1][21];
        for(int i = 0; i < n+1; i ++) {
            table[i] = Arrays.copyOf(other.table[i], 21);
        }
        numChecksInRow = Arrays.copyOf(other.numChecksInRow, other.n+1);
        numXsInRow = Arrays.copyOf(other.numXsInRow, other.n+1);
        handSizes = Arrays.copyOf(other.handSizes, other.n+1);
        numAnswerTypeXs = Arrays.copyOf(other.numAnswerTypeXs, 3);
        numXsInCol = Arrays.copyOf(other.numXsInCol, 21);
        colHasCheck = Arrays.copyOf(other.colHasCheck, 21);
    }

    /**sets check if value is true, sets X otherwise*/
    public boolean setValue(int player, int card, boolean value) {
        if(value) {
            return setCheck(player, card);
        } else {
            return setX(player, card);
        }
    }

    /**
     * Sets a check on the table for the player and card
     * @return true if success, false if failure (impossible check)
     */
    public boolean setCheck(int player, int card) {
        if(table[player][card] != null) { //if there's already something there
            return table[player][card];
        }
        table[player][card] = true;
        numChecksInRow[player] ++;
        colHasCheck[card] = true;

        ArrayList<int[]> scheduledXs = new ArrayList<>();
        for(int i = 0; i < n+1; i ++) { //put Xs in the column
            if(table[i][card] == null) scheduledXs.add(new int[]{i, card});
        }
        if(player == n) { //if adding a check to the answer hand, put Xs in the rest of that card type
            int[] interval = getTypeInterval(card);
            for(int i = interval[0]; i <= interval[1]; i ++) {
                if(table[n][i] == null) scheduledXs.add(new int[]{n, i});
            }
        } else if(numChecksInRow[player] == handSizes[player]) { //if hand full, put Xs in the row
            for(int i = 0; i < 21; i ++) {
                if(table[player][i] == null) scheduledXs.add(new int[]{player, i});
            }
        }

        for(int[] x : scheduledXs) { // X all scheduled entries
            if(!setX(x[0], x[1])) return false;
        }
        return true;
    }


    /**
     * Sets an X on the table for the player and card
     * @return true if success, false if failure (impossible X)
     */
    public boolean setX(int player, int card) {
        if(table[player][card] != null) {
            return !table[player][card];
        }
        table[player][card] = false;
        numXsInRow[player] ++;
        numXsInCol[card] ++;
        if(player == n) numAnswerTypeXs[getType(card)] ++;

        ArrayList<int[]> scheduledChecks = new ArrayList<>();
        if(player != n && numXsInRow[player] + handSizes[player] == 21) { //if there are enough Xs to where the rest should be checks
            for(int i = 0; i < 21; i ++) {
                if(table[player][i] == null) scheduledChecks.add(new int[]{player, i});
            }
        }
        if(player == n && numAnswerTypeXs[getType(card)] == getSizeOfType(card) - 1) { //if there are enough Xs in the answer type to where the rest should be checks
            int[] interval = getTypeInterval(card);
            for(int i = interval[0]; i <= interval[1]; i ++) {
                if(table[n][i] == null) scheduledChecks.add(new int[]{n, i});
            }
        }
        if(numXsInCol[card] == n && !colHasCheck[card]) { //if only one entry left in the column for the check
            for(int i = 0; i < n+1; i ++) {
                if(table[i][card] == null) scheduledChecks.add(new int[]{i, card});
            }
        }

        for(int[] check : scheduledChecks) { // Check all scheduled entries
            if(!setCheck(check[0], check[1])) return false;
        }
        return true;
    }

    /**tells you if a card is a suspect, weapon, or room*/
    private int getType(int card) {
        return card < 6? 0 :
                card < 12? 1 : 2;
    }

    /**tells you how many cards there are of that type*/
    private int getSizeOfType(int card) {
        return card < 12? 6 : 9;
    }

    /**tells you what interval (inclusive) of the card's type is*/
    private int[] getTypeInterval(int card) {
        return card < 6? new int[] {0, 5} :
                card < 12? new int[] {6, 11} :
                        new int[] {12, 20};
    }

    public Boolean getTableEntry(int player, int card) {
        return table[player][card];
    }
    public int[] getHandSizes() {return Arrays.copyOf(handSizes, n);}

    public String difference(Hands other) {
        StringBuilder result = new StringBuilder("\t\t");
        for(int i = 0; i < 21; i ++) {
            result.append(i).append("\t");
        }
        result.append("\n     ———————————————————————————————————————————————————————————————————————————————————————\n");
        for(int i = 0; i < n+1; i ++) {
            result.append(i).append("   |\t");
            for(int j = 0; j < table[i].length; j ++) {
                if(table[i][j] == null && other.table[i][j] != null) {
                    result.append(other.table[i][j]? "✔" : "✘");
                }
                result.append("\t");
            }
            result.append("\n    | \n");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    public void print() {
        System.out.println(this);
    }

    public void setCardsInLogic(HashSet<Integer> cardsInLogic) {this.cardsInLogic = cardsInLogic;}
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\t\t");
        for(int i = 0; i < 21; i ++) {
            result.append(cardToString[i]).append("\t");
            if(i == 5 || i == 11) result.append("|\t");
        }
        result.append("\n     —————————————————————————————————————————————————————————————————————————————————————————————\n");
        for(int i = 0; i < n+1; i ++) {
            result.append(i).append("   |\t");
            for(int j = 0; j < table[i].length; j++) {
                result.append(table[i][j] == null ?
                        cardsInLogic.contains(i*21+j) ? "_" : "."
                        : table[i][j]? "✔" : "✘").append("\t");
                if(j == 5 || j == 11) result.append("|\t");
            }
            result.append("\n    |\t\t\t\t\t\t\t|\t\t\t\t\t\t\t| \n");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }
}