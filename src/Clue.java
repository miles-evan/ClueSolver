/**
 * Handles the hands table and logic, and manipulates these based off of game moves.
 * Uses the backtracker to intelligently update the hands based off of the information we know.
 * @author Miles Todtfeld
 */
public class Clue {

    /** number of players */
    private final int n;
    /** the information we have so far about which players do or don't have which cards */
    private final Hands hands;
    /** the hands, but if we did not use backtracking. serves as a way to see how much the backtracking helped */
    private final Hands primitiveTable;
    /** the information we have so far about which players may have which cards,
     * and which cards may not be in the answer hand */
    private final ClueLogic logic;
    /** runs the backtracking algorithm to find new information */
    private final ClueBacktracker backtracker;
    /** if this is false, a check or X was set that is impossible to have set.
     * if that happens in a game, someone probably accidentally lied */
    private boolean valid = true;


    public Clue(int n, int ... handSizes) {
        this.n = n;
        hands = new Hands(n, handSizes);
        primitiveTable = new Hands(n, handSizes);
        logic = new ClueLogic(n);
        backtracker = new ClueBacktracker(n, 5000, hands, logic);
        hands.setCardsInLogic(logic.getCardsInLogic());
    }
    public Clue() {
        this(6, 3, 3, 3, 3, 3, 3);
    }

    /**
     * when a player makes a suggestion, and we don't know which card was handed over
     * @param player which player made the suggestion
     * @param suspect the suspect card they suggested
     * @param weapon the weapon card they suggested
     * @param room the room card they suggested
     * @param numTries the number of players that it took for someone to have one of the cards
     *                 (including the player who had the card)
     *                 ex: numTries would be 2 if the first person said they didn't have the cards, but the next did.
     *                 ex2: numTries would be -1 if nobody had any of the cards
     */
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

    /**
     * when a player makes a suggestion, and we know which card was handed over
     * @param player which player made the suggestion
     * @param suspect the suspect card they suggested
     * @param weapon the weapon card they suggested
     * @param room the room card they suggested
     * @param numTries the number of players that it took for someone to have one of the cards
     *                 (including the player who had the card)
     *                 ex. numTries would be 2 if the first person said they didn't have the cards, but the next did
     * @param cardHandedOver the card that was handed to the user
     */
    public void addInfo(int player, int suspect, int weapon, int room, int numTries, int cardHandedOver) {
        for(int i = 1; i < numTries; i ++) {
            setX((player + i) % n, suspect);
            setX((player + i) % n, weapon);
            setX((player + i) % n, room);
        }
        setCheck((player + numTries) % n, cardHandedOver);
        update();
    }
    /** when a player makes an incorrect accusation */
    public void incorrectAccusation(int player, int suspect, int weapon, int room) {
        setX(player, suspect);
        setX(player, weapon);
        setX(player, room);
        logic.addAccusation(suspect, weapon, room);
        update();
    }

    /** goes through each table entry and runs testEntry() on it */
    public void update() {
        backtracker.setStartTime(System.currentTimeMillis());
        boolean changed;
        do {
            changed = false;
            for (int player = 0; player < n + 1; player++) {
                for (int card = 0; card < 21; card++) {
                    if (testEntry(player, card)) changed = true;
                }
            }
            if(changed) System.out.println("\nBacktracking algorithm just found new information!\n");
        } while (backtracker.isTimeLimitExceeded() && changed);

    }
    /** uses the backtracker to see if a table entry must be a check or X based on the info we have */
    private boolean testEntry(int player, int card) {
        if(hands.getTableEntry(player, card) != null) {
            return false;
        } else if(!backtracker.possible(player, card, true)) {
            hands.setX(player, card);
            return true;
        } else if(!backtracker.possible(player, card, false)) {
            hands.setCheck(player, card);
            return true;
        }
        return false;
    }


    public boolean setCheck(int player, int card) {
        if(!backtracker.possible(player, card, true)) valid = false;
        primitiveTable.setCheck(player, card);
        return hands.setCheck(player, card);
    }
    public boolean setX(int player, int card) {
        if(!backtracker.possible(player, card, false)) valid = false;
        primitiveTable.setX(player, card);
        return hands.setX(player, card);
    }

    public void setTimeLimit(long timeLimit) {
        backtracker.setTimeLimit(timeLimit);
    }

    public boolean isValid() {
        return valid;
    }

    public void printTable() {
        hands.print();
    }
    public void print() {
        logic.print();
        hands.print();
    }
    public void printPrimitiveTable() {
        primitiveTable.print();
    }
    public void printLogic() {
        logic.print();
    }
    public void printDifference() {
        System.out.println(primitiveTable.difference(hands));
    }
    @Override
    public String toString() {
        return logic.toString() + hands.toString();
    }
}