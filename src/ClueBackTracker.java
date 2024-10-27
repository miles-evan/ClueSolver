public class ClueBackTracker {
    private final int n;
    private final Hands hands;
    private final ClueLogic logic;

    public ClueBackTracker(int n, Hands hands, ClueLogic logic) {
        this.n = n;
        this.hands = hands;
        this.logic = logic;
    }



    public boolean possible(int player, int card, boolean value) {
        Hands newHands = new Hands(hands);
        if(!newHands.setValue(player, card, value)) return false;
        return possible(newHands);
    }
    private boolean possible(Hands currentHands) {
        return possible(currentHands, new boolean[logic.size()], 0, false) ||
                possible(currentHands, new boolean[logic.size()], 0, true);
    }
    private boolean possible(Hands currentHands, boolean[] currentTruths, int index, boolean value) {
        if(index == logic.size()) return accusationPossible(currentHands);

        Hands newHands = new Hands(currentHands);
        if(!newHands.setValue(logic.getPlayer(index), logic.getCard(index), value)) return false;
        currentTruths[index] = value;
        if(index % 3 == 2 && !(currentTruths[index-2] || currentTruths[index-1] || currentTruths[index])) return false;

        return possible(newHands, currentTruths, index + 1, true) ||
                possible(newHands, currentTruths, index + 1, false);
    }

    private boolean accusationPossible(Hands currentHands) {
        return accusationPossible(currentHands, 0, false) ||
                accusationPossible(currentHands, 0, true);
    }
    private boolean accusationPossible(Hands currentHands, int index, boolean value) {
        if(index == logic.accusationSize()) return true;

        Hands newHands = new Hands(currentHands);
        if(!newHands.setValue(n, logic.getAccusation(index), value)) return false;
        if(index % 3 == 2 && newHands.getTableEntry(n, logic.getAccusation(index)) &&
                newHands.getTableEntry(n, logic.getAccusation(index - 1)) &&
                newHands.getTableEntry(n, logic.getAccusation(index - 2))) return false;

        return accusationPossible(newHands, index + 1, false) ||
                accusationPossible(newHands, index + 1, true);
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
}
