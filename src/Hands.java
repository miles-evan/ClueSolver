import java.util.ArrayList;
import java.util.Arrays;

public class Hands {
    private final int n;
    private final Boolean[][] table;
    private final int[] numChecks;
    private final int[] numXs;
    private final int[] handSizes;
    private final int[] numAnswerTypeXs = new int[3];
    private final int[] numXsInCol = new int[21];
    private final boolean[] colHasCheck = new boolean[21];

    public Hands() {
        this(6, 3, 3, 3, 3, 3, 3);
    }
    public Hands(int n, int ... handSizes) {
        this.n = n;
        table = new Boolean[n+1][21];
        numChecks = new int[n+1];
        numXs = new int[n+1];
        this.handSizes = new int[n+1];
        System.arraycopy(handSizes, 0, this.handSizes, 0, n);
        this.handSizes[n] = 3;
    }
    public Hands(Hands other) {
        n = other.n;
        table = new Boolean[n+1][21];
        for(int i = 0; i < n+1; i ++) {
            table[i] = Arrays.copyOf(other.table[i], 21);
        }
        numChecks = Arrays.copyOf(other.numChecks, other.n+1);
        numXs = Arrays.copyOf(other.numXs, other.n+1);
        handSizes = Arrays.copyOf(other.handSizes, other.n+1);
    }

    public boolean setValue(int player, int card, boolean value) {
        if(value) {
            return setCheck(player, card);
        } else {
            return setX(player, card);
        }
    }

    public boolean setCheck(int player, int card) {
        if(table[player][card] != null) { //if there's already something there
            return table[player][card];
        }
        table[player][card] = true;
        numChecks[player] ++;
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
        } else if(numChecks[player] == handSizes[player]) { //if hand full, put Xs in the row
            for(int i = 0; i < 21; i ++) {
                if(table[player][i] == null) scheduledXs.add(new int[]{player, i});
            }
        }

        for(int[] x : scheduledXs) { // X all scheduled entries
            if(!setX(x[0], x[1])) return false;
        }
        return true;
    }


    public boolean setX(int player, int card) {
        if(table[player][card] != null) {
            return !table[player][card];
        }
        table[player][card] = false;
        numXs[player] ++;
        numXsInCol[card] ++;
        if(player == n) numAnswerTypeXs[getType(card)] ++;

        ArrayList<int[]> scheduledChecks = new ArrayList<>();
        if(player != n && numXs[player] + handSizes[player] == 21) { //if there are enough Xs to where the rest should be checks
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


    private int getType(int card) {
        return card < 6? 0 :
                card < 12? 1 : 0;
    }
    private int getSizeOfType(int card) {
        return card < 12? 6 : 9;
    }
    private int[] getTypeInterval(int card) {
        return card < 6? new int[] {0, 5} :
                card < 12? new int[] {6, 11} :
                        new int[] {12, 20};
    }

    public Boolean getTableEntry(int player, int card) {
        return table[player][card];
    }
    public int getN() {return n;}
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\t\t");
        for(int i = 0; i < 21; i ++) {
            result.append(i).append("\t");
        }
        result.append("\n     ———————————————————————————————————————————————————————————————————————————————————————\n");
        for(int i = 0; i < n+1; i ++) {
            result.append(i).append("   |\t");
            for(Boolean card : table[i]) {
                result.append(card == null? "." : card? "✔" : "✘").append("\t");
            }
            result.append("\n    | \n");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }
}