package clueSolvingAlgorithm;

/**
 * Conducts backtracking to determine if it's possible for a certain player to have (check) or not have (X) some card
 * while satisfying the constraints and information we know
 * @author Miles Todtfeld
 */
public class ClueBacktracker {
    /** number of players */
    private final int n;

    /** the information we have so far about which players do or don't have which cards */
    private final Hands hands;

    /** the information we have so far about which players may have which cards,
     * and which cards may not be in the answer hand */
    private final ClueLogic logic;
    /** time of starting some task that is wanted to be timed */
    private long startTime = 0;

    /**
     * once this amount of time has passed, stop doing the "complete" backtracking
     * because it's very slow.
     * if timeLimit = -1, then there is no time limit
     */
    private long timeLimit;
    /** whether or not the time limit has exceeded. if it has stop doing the "complete" backtracking */
    private boolean timeLimitExceeded = false;

    public ClueBacktracker(int n, long timeLimit, Hands hands, ClueLogic logic) {
        this.n = n;
        this.timeLimit = timeLimit;
        this.hands = hands;
        this.logic = logic;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
        timeLimitExceeded = false;
    }
    public boolean isTimeLimitExceeded() {
        return timeLimitExceeded;
    }


    /** @return true if it's possible to place a check or X in a certain location
     * while satisfying constraints we know */
    public boolean possible(int player, int card, boolean value) {
        Hands newHands = new Hands(hands);
        if(!newHands.setValue(player, card, value)) return false;
        return possible(newHands);
    }
    /** takes a Clue.Hands that has been altered by one entry, and returns if it's possible
     * to fill in the rest of the table while satisfying constraints we know */
    private boolean possible(Hands currentHands) {
        return possible(currentHands, new boolean[logic.size()], 0, false) ||
                possible(currentHands, new boolean[logic.size()], 0, true);
    }

    /**
     * backtracking first on the logic variables
     * @param currentHands the table we have been playing around with. copied at each method call
     * @param currentTruths which logic vars we've made true or false so far. (at least one of each three must be true)
     * @param index which log var we are up to
     * @param value the value we are trying to set the logic var to
     * @return true if possible, false otherwise
     */
    private boolean possible(Hands currentHands, boolean[] currentTruths, int index, boolean value) {
        if(index == logic.size()) return accusationPossible(currentHands);

        currentHands = new Hands(currentHands);
        if(!currentHands.setValue(logic.getPlayer(index), logic.getCard(index), value)) return false;
        currentTruths[index] = value;
        if(index % 3 == 2 && !(currentTruths[index-2] || currentTruths[index-1] || currentTruths[index])) return false;

        return possible(currentHands, currentTruths, index + 1, true) ||
                possible(currentHands, currentTruths, index + 1, false);
    }

    /**
     * @return if it's possible to satisfy the fact that accusation that has been made was wrong
     */
    private boolean accusationPossible(Hands currentHands) {
        return accusationPossible(currentHands, 0, false) ||
                accusationPossible(currentHands, 0, true);
    }

    /**
     * backtracking on the accusation logic variables
     * @param currentHands the table we have been playing around with. copied at each method call
     * @param index which log var we are up to
     * @param value the value we are trying to set the accusation logic var to
     * @return true if possible, false otherwise
     */
    private boolean accusationPossible(Hands currentHands, int index, boolean value) {
        if(index == logic.accusationSize()) return completePossible(currentHands);

        currentHands = new Hands(currentHands);
        if(!currentHands.setValue(n, logic.getAccusation(index), value)) return false;
        if(index % 3 == 2 && currentHands.getTableEntry(n, logic.getAccusation(index)) &&
                currentHands.getTableEntry(n, logic.getAccusation(index - 1)) &&
                currentHands.getTableEntry(n, logic.getAccusation(index - 2))) return false;

        return accusationPossible(currentHands, index + 1, false) ||
                accusationPossible(currentHands, index + 1, true);
    }

    /**
     * @return if it's possible to fill in the rest of the table with checks and Xs
     * while following the basic constraints of the clue table
     */
    private boolean completePossible(Hands currentHands) {
        return completePossible(currentHands, 0, 0, true) ||
                completePossible(currentHands, 0, 0, false);
    }

    /**
     * backtracking on the rest of the table
     * @param currentHands the table we have been playing around with. copied at each method call
     * @param player the row of the entry we are up to
     * @param card the column of the entry we are up to
     * @param value the value we are trying to set the entry to
     * @return true if possible, false otherwise
     */
    private boolean completePossible(Hands currentHands, int player, int card, boolean value) {
        if(timeLimit != -1 && System.currentTimeMillis() - startTime > timeLimit) {
            timeLimitExceeded = true;
            return true;
        }

        if(card == 21) {
            player ++;
            card = 0;
        }
        if(player == n+1) return true;

        currentHands = new Hands(currentHands);
        if(currentHands.getTableEntry(player, card) == null && !currentHands.setValue(player, card, value)) return false;

        return completePossible(currentHands, player, card + 1, true) ||
                completePossible(currentHands, player, card + 1, false);
    }

}
