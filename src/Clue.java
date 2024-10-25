import java.util.ArrayList;
import java.util.Arrays;

public class Clue {

    private final int n;
    private final Hands hands;
    private final Hands primitiveTable;
    private final ArrayList<Integer>[] logic;
    private int numLogicVars = 0;
    private final ArrayList<Integer> accusationLogic;


    public Clue(int n, int ... handSizes) {
        this.n = n;
        hands = new Hands(n, handSizes);
        primitiveTable = new Hands(n, handSizes);
        logic = new ArrayList[n];
        for(int i = 0; i < n; i ++) {
            logic[i] = new ArrayList<>();
        }
        accusationLogic = new ArrayList<>();
    }
    public Clue() {
        this(6, 3, 3, 3, 3, 3, 3);
    }

    public void addInfo(int player, int suspect, int weapon, int room, int numTries) {
        for(int i = 1; i < (numTries == -1? n : numTries); i ++) {
            setX((player + i) % n, suspect);
            setX((player + i) % n, weapon);
            setX((player + i) % n, room);
        }
        if(numTries == -1) return;
        logic[(player + numTries) % n].add(suspect);
        logic[(player + numTries) % n].add(weapon);
        logic[(player + numTries) % n].add(room);
        numLogicVars += 3;
        update();
    }

    public void addInfo(int player, int suspect, int weapon, int room, int numTries, int cardHandedOver) {
        for(int i = 1; i < numTries; i ++) {
            setX((player + i) % n, suspect);
            setX((player + i) % n, weapon);
            setX((player + i) % n, room);
        }
        setCheck((player + numTries) % n, cardHandedOver);
        update();
    }

    public void incorrectAccusation(int suspect, int weapon, int room) {
        accusationLogic.add(suspect);
        accusationLogic.add(weapon);
        accusationLogic.add(room);
        update();
    }

    public void update() {
        boolean changed = false;
        for(int player = 0; player < n+1; player++) {
            for(int card = 0; card < 21; card++) {
                if(testEntry(player, card)) changed = true;
            }
        }
        if(changed) update();
    }
    private boolean testEntry(int player, int card) {
        if(hands.getTableEntry(player, card) != null) {
            return false;
        } else if(!possible(player, card, true)) {
            hands.setX(player, card);
            return true;
        } else if(!possible(player, card, false)) {
            hands.setCheck(player, card);
            return true;
        }
        return false;
    }
    private boolean possible(int player, int card, boolean value) {
        Hands newHands = new Hands(hands);
        if(!newHands.setValue(player, card, value)) return false;
        return possible(newHands);
    }
    private boolean possible(Hands currentHands) {
        return possible(currentHands, new boolean[numLogicVars], 0, 0, 0, false) ||
                possible(currentHands, new boolean[numLogicVars], 0, 0, 0, true);
    }
    private boolean possible(Hands currentHands, boolean[] currentTruths, int i, int player, int logicVarSubIndex, boolean value) {
        while(logic[player].isEmpty()) {
            player ++;
            if(player == n) return accusationPossible(currentHands);
        }

        Hands newHands = new Hands(currentHands);
        if(!newHands.setValue(player, logic[player].get(logicVarSubIndex), value)) return false;
        currentTruths[i] = value;
        if(i % 3 == 2 && !(currentTruths[i-2] || currentTruths[i-1] || currentTruths[i])) return false;

        if(++i == currentTruths.length) return accusationPossible(newHands);
        if(++logicVarSubIndex == logic[player].size()) {
            player ++;
            logicVarSubIndex = 0;
        }
        return possible(newHands, currentTruths, i, player, logicVarSubIndex, true) ||
                possible(newHands, currentTruths, i, player, logicVarSubIndex, false);
    }

    private boolean accusationPossible(Hands currentHands) {
        return accusationPossible(currentHands, 0, false) ||
                accusationPossible(currentHands, 0, true);
    }
    private boolean accusationPossible(Hands currentHands, int i, boolean value) {
        if(i == accusationLogic.size()) return true;
        Hands newHands = new Hands(currentHands);
        if(!newHands.setValue(n, accusationLogic.get(i), value)) return false;
        if(i % 3 == 2 && newHands.getTableEntry(n, accusationLogic.get(i)) &&
                newHands.getTableEntry(n, accusationLogic.get(i - 1)) &&
                newHands.getTableEntry(n, accusationLogic.get(i - 2))) return false;
        return accusationPossible(newHands, i + 1, false) ||
                accusationPossible(newHands, i + 1, true);
    }

    private boolean completePossible(Hands currentHands) {
        return completePossible(currentHands, 0, 0, true) ||
                completePossible(currentHands, 0, 0, false);
    }
    private boolean completePossible(Hands currentHands, int player, int card, boolean value) {
        /*
        if(System.currentTimeMillis() - time > 100) {
            timeLimitExceeded = true;
            return true;
        }
        */

        while(currentHands.getTableEntry(player, card) != null) {
            if(++card == 21) {
                card = 0;
                if(++player == n+1) {
                    return true;
                }
            }
        }

        Hands newHands = new Hands(currentHands);
        if(!newHands.setValue(player, card, value)) return false;
        if(++card == 21) {
            card = 0;
            if(++player == n+1) {
                return true;
            }
        }
        return completePossible(newHands, player, card, true) ||
                completePossible(newHands, player, card, false);
    }





    public void setValue(int player, int card, boolean value) {
        hands.setValue(player, card, value);
        primitiveTable.setValue(player, card, value);
    }
    public void setCheck(int player, int card) {
        hands.setCheck(player, card);
        primitiveTable.setCheck(player, card);
    }
    public void setX(int player, int card) {
        hands.setX(player, card);
        primitiveTable.setX(player, card);
    }


    public void printTable() {
        System.out.println(hands);
    }

    public void printLogic() {
        for(int i = 0; i < n; i ++) {
            System.out.print("Player " + i + ": ");
            for(int j = 0; j < logic[i].size();) {
                System.out.print("(" + logic[i].get(j++) + " " + logic[i].get(j++) + " " + logic[i].get(j++) + ") ");
            }
            System.out.println();
        }
        System.out.print("Accusations: ");
        for(int i = 0; i < accusationLogic.size(); i += 3) {
            System.out.print("(" + accusationLogic.get(i++) + " " + accusationLogic.get(i++) + " " + accusationLogic.get(i++) + ") ");
        }
        System.out.println();
    }

    public void print() {
        printLogic();
        printTable();
    }

    public void printPrimitiveTable() {
        System.out.println(primitiveTable);
    }
    public void printDifference() {
        System.out.println(primitiveTable.difference(hands));
    }
}