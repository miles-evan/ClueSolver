public class ClueBackTracker {
    private final int n;
    private final Hands hands;
    private final ClueLogic logic;
    private long startTime = 0;
    private long timeLimit;

    public ClueBackTracker(int n, long timeLimit, Hands hands, ClueLogic logic) {
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
    }


    public boolean possible(int player, int card, boolean value) {
        Hands newHands = new Hands(hands);
        if(!newHands.setValue(player, card, value)) return false;
        return possible(newHands);
    }
    public boolean possible(Hands currentHands) {
        return possible(currentHands, new boolean[logic.size()], 0, false) ||
                possible(currentHands, new boolean[logic.size()], 0, true);
    }
    private boolean possible(Hands currentHands, boolean[] currentTruths, int index, boolean value) {
        if(index == logic.size()) return accusationPossible(currentHands);

        currentHands = new Hands(currentHands);
        if(!currentHands.setValue(logic.getPlayer(index), logic.getCard(index), value)) return false;
        currentTruths[index] = value;
        if(index % 3 == 2 && !(currentTruths[index-2] || currentTruths[index-1] || currentTruths[index])) return false;

        return possible(currentHands, currentTruths, index + 1, true) ||
                possible(currentHands, currentTruths, index + 1, false);
    }

    private boolean accusationPossible(Hands currentHands) {
        return accusationPossible(currentHands, 0, false) ||
                accusationPossible(currentHands, 0, true);
    }
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

    private boolean completePossible(Hands currentHands) {
        return completePossible(currentHands, 0, 0, true) ||
                completePossible(currentHands, 0, 0, false);
    }
    private boolean completePossible(Hands currentHands, int player, int card, boolean value) {
        if(timeLimit != -1 && System.currentTimeMillis() - startTime > timeLimit) return true;

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
