public class Clue {

    private final int n;
    private final Hands hands;
    private final Hands primitiveTable;
    private final ClueLogic logic;
    private final ClueBackTracker backTracker;


    public Clue(int n, int ... handSizes) {
        this.n = n;
        hands = new Hands(n, handSizes);
        primitiveTable = new Hands(n, handSizes);
        logic = new ClueLogic(n);
        backTracker = new ClueBackTracker(n, 5000, hands, logic);

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
        logic.add((player + numTries) % n, suspect, weapon, room);
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
        logic.addAccusation(suspect, weapon, room);
        update();
    }

    public void update() {
        backTracker.setStartTime(System.currentTimeMillis());
        boolean changed = true;
        while(changed) {
            changed = false;
            for (int player = 0; player < n + 1; player++) {
                for (int card = 0; card < 21; card++) {
                    if (testEntry(player, card)) changed = true;
                }
            }
        }
    }
    private boolean testEntry(int player, int card) {
        if(hands.getTableEntry(player, card) != null) {
            return false;
        } else if(!backTracker.possible(player, card, true)) {
            hands.setX(player, card);
            return true;
        } else if(!backTracker.possible(player, card, false)) {
            hands.setCheck(player, card);
            return true;
        }
        return false;
    }


    public void setCheck(int player, int card) {
        hands.setCheck(player, card);
        primitiveTable.setCheck(player, card);
    }
    public void setX(int player, int card) {
        hands.setX(player, card);
        primitiveTable.setX(player, card);
    }


    public void setTimeLimit(long timeLimit) {
        backTracker.setTimeLimit(timeLimit);
    }

    public void printTable() {
        System.out.println(hands);
    }
    public void print() {
        logic.print();
        printTable();
    }
    public void printPrimitiveTable() {
        System.out.println(primitiveTable);
    }
    public void printDifference() {
        System.out.println(primitiveTable.difference(hands));
    }
}