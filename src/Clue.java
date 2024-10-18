import java.util.ArrayList;

public class Clue {

    private final int n;
    private final Hands hands;
    private final Hands primitiveTable;
    private final ArrayList<Integer>[] logic;
    private int numLogicVars = 0;


    public Clue(int n, int ... handSizes) {
        this.n = n;
        hands = new Hands(n, handSizes);
        primitiveTable = new Hands(n, handSizes);
        logic = new ArrayList[n];
        for(int i = 0; i < n; i ++) {
            logic[i] = new ArrayList<>();
        }
    }
    public Clue() {
        this(6, 3, 3, 3, 3, 3, 3);
    }


    public void addInfo(int player, int suspect, int weapon, int room, int numTries) {
        for(int i = 1; i < (numTries == -1? n : numTries); i ++) {
            hands.setX((player + i) % n, suspect);
            hands.setX((player + i) % n, weapon);
            hands.setX((player + i) % n, room);
            primitiveTable.setX((player + i) % n, suspect);
            primitiveTable.setX((player + i) % n, weapon);
            primitiveTable.setX((player + i) % n, room);
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
            hands.setX((player + i) % n, suspect);
            hands.setX((player + i) % n, weapon);
            hands.setX((player + i) % n, room);
            primitiveTable.setX((player + i) % n, suspect);
            primitiveTable.setX((player + i) % n, weapon);
            primitiveTable.setX((player + i) % n, room);
        }
        hands.setCheck((player + numTries) % n, cardHandedOver);
        primitiveTable.setCheck((player + numTries) % n, cardHandedOver);
        update();
    }


    public void update() {
        boolean changed = false;
        for(int player = 0; player < n; player++) {
            for(int card = 0; card < 21; card++) {
                if(testEntry(player, card)) {
                    changed = true;
                }
            }
        }
        if(changed) {
            update();
        }
    }
    public boolean testEntry(int player, int card) {
        if(hands.getTableEntry(player, card) != null) {
            return false;
        }
        if(!possible(player, card, true)) {
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
            if(player == n) return true;
        }

        Hands newHands = new Hands(currentHands);
        if(!newHands.setValue(player, logic[player].get(logicVarSubIndex), value)) return false;
        currentTruths[i] = value;
        if(i % 3 == 2 && !(currentTruths[i-2] || currentTruths[i-1] || currentTruths[i])) return false;

        if(++i == currentTruths.length) {
            return true;
        }
        if(++logicVarSubIndex == logic[player].size()) {
            player ++;
            logicVarSubIndex = 0;
        }
        return possible(newHands, currentTruths, i, player, logicVarSubIndex, true) ||
                possible(newHands, currentTruths, i, player, logicVarSubIndex, false);
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