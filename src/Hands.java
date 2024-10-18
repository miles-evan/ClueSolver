import java.util.ArrayList;
import java.util.Arrays;

public class Hands {
    private final int n;
    private Boolean[][] table;
    private final int[] numChecks;
    private final int[] numXs;
    private final int[] numEmpty;
    private final int[] handSizes;

    public Hands() {
        this(6, 3, 3, 3, 3, 3, 3);
    }
    public Hands(int n, int ... handSizes) {
        this.n = n;
        table = new Boolean[n][21];
        numChecks = new int[n];
        numXs = new int[n];
        numEmpty = new int[n];
        for(int i = 0; i < n; i ++) {
            numEmpty[i] = 21;
        }
        this.handSizes = handSizes;
    }
    public Hands(Hands other) {
        n = other.n;
        table = new Boolean[n][21];
        for(int i = 0; i < n; i ++) {
            table[i] = Arrays.copyOf(other.table[i], 21);
        }
        numChecks = Arrays.copyOf(other.numChecks, other.n);
        numXs = Arrays.copyOf(other.numXs, other.n);
        numEmpty = Arrays.copyOf(other.numEmpty, other.n);
        handSizes = Arrays.copyOf(other.handSizes, other.n);
    }

    public boolean setValue(int player, int card, boolean value) {
        if(value) {
            return setCheck(player, card);
        } else {
            return setX(player, card);
        }
    }
    public boolean setCheck(int player, int card) {
        if(table[player][card] != null) {
            return table[player][card];
        } else if(numChecks[player] >= handSizes[player]) { //will this ever run? (i don't think so)
            return false;
        }
        table[player][card] = true;
        numChecks[player] ++;
        numEmpty[player] --;
        for(int i = 0; i < n; i ++) {
            setX(i, card);
        }
        if(numChecks[player] == handSizes[player]) {
            for(int i = 0; i < 21; i ++) {
                setX(player, i); //does this work? i haven't tested it yet
            }
        }
        return true;
    }

    public boolean setX(int player, int card) {
        if(table[player][card] != null) {
            return !table[player][card];
        }
        table[player][card] = false;
        numXs[player] ++;
        numEmpty[player] --;
        boolean valid = true;
        if(numXs[player] + handSizes[player] == 21) {
            for(int i = 0; i < 21; i ++) {
                if(table[player][i] == null && !setCheck(player, i)) { //will this ever run?
                    valid = false;
                }
                if(numXs[player] + handSizes[player] > 21) {
                    valid = false;
                }
            }
        }
        return valid;
    }


    public int getN() {
        return n;
    }
    public Boolean getTableEntry(int player, int card) {
        return table[player][card];
    }
    public int getNumChecks(int i) {
        return numChecks[i];
    }
    public int getNumXs(int i) {
        return numXs[i];
    }
    public int getNumEmpty(int i) {
        return numEmpty[i];
    }
    public int getHandSize(int i) {
        return handSizes[i];
    }

    public String difference(Hands other) {
        StringBuilder result = new StringBuilder("\t\t");
        for(int i = 0; i < 21; i ++) {
            result.append(i).append("\t");
        }
        result.append("\n     ———————————————————————————————————————————————————————————————————————————————————————\n");
        for(int i = 0; i < n; i ++) {
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
        for(int i = 0; i < n; i ++) {
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
