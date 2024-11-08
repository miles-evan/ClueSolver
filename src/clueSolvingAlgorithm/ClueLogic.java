package clueSolvingAlgorithm;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Holds the logic of when players hand over one of three cards, but you don't know which one,
 * as well as when a player makes an incorrect accusation, so you know at least one of the three cards are incorrect.
 * @author Miles Todtfeld
 */
public class ClueLogic {
    /** number of players */
    private final int n;

    /**
     * holds the strings of logic for each player.
     * each number represents a card.
     * ex: [0, 6, 12, 0, 6, 20] means:
     * that player has cards (0 or 6 or 12) and (0 or 6 or 20)
     */
    private final ArrayList<Integer>[] logic;
    /** equal to the sum of the sizes of logic[i]s */
    private int numLogicVars = 0;
    /**
     * holds the string of logic representing failed accusations.
     * every time a player makes an incorrect accusation, one of those 3 card must not be in the answer hand.
     * so for example [0, 6, 12, 0, 6, 20] means
     * the answer has cards (not 0 or not 6 or not 12) and (not 0 or not 6 or not 20)
     */
    private final ArrayList<Integer> accusationLogic;
    /** maps the number of cards to their abbreviations */
    private final String[] cardToString = new String[]{"gr","mu","or","pe","pl","sc","ca","da","le","re","ro","wr",
            "ba","bi","co","di","ha","ki","li","lo","st"};
    /** Tells you which card have been added to the logic and which have not */
    private final HashSet<Integer> cardsInLogic = new HashSet<>();

    public ClueLogic(int n) {
        this.n = n;
        logic = new ArrayList[n];
        for(int i = 0; i < n; i ++) {
            logic[i] = new ArrayList<>();
        }
        accusationLogic = new ArrayList<>();
    }
    public ClueLogic() {
        this(6);
    }

    /** when a player hands over one of three cards, but we don't know which one */
    public void add(int player, int suspect, int weapon, int room) {
        logic[player].add(suspect);
        logic[player].add(weapon);
        logic[player].add(room);
        numLogicVars += 3;
        cardsInLogic.add(player*21+suspect);
        cardsInLogic.add(player*21+weapon);
        cardsInLogic.add(player*21+room);
    }

    /** when a player makes an accusation but is wrong */
    public void addAccusation(int suspect, int weapon, int room) {
        accusationLogic.add(suspect);
        accusationLogic.add(weapon);
        accusationLogic.add(room);
    }


    public int size() {
        return numLogicVars;
    }
    public int accusationSize() {
        return accusationLogic.size();
    }
    /** @return the card corresponded to the index-th accusation logic var */
    public int getAccusation(int index) {
        return accusationLogic.get(index);
    }
    /** @return the player corresponded to the index-th logic var */
    public int getPlayer(int index) {
        return get(0, index)[0];
    }
    /** @return the card corresponded to the index-th logic var */
    public int getCard(int index) {
        return get(0, index)[1];
    }
    /** @return the player and card corresponded to the index-th logic var */
    private int[] get(int player, int index) {
        if(index >= logic[player].size())
            return get(player + 1, index - logic[player].size());
        return new int[] {player, logic[player].get(index)};
    }
    public HashSet<Integer> getCardsInLogic() {return cardsInLogic;}

    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < n; i++) {
            result.append("Player ").append(i).append(": ");
            for (int j = 0; j < logic[i].size();) {
                result.append("(")
                        .append(cardToString[logic[i].get(j++)]).append(" ")
                        .append(cardToString[logic[i].get(j++)]).append(" ")
                        .append(cardToString[logic[i].get(j++)]).append(") ");
            }
            result.append("\n");
        }

        result.append("Accusations: ");
        for (int i = 0; i < accusationLogic.size(); i += 3) {
            result.append("(")
                    .append(cardToString[accusationLogic.get(i++)]).append(" ")
                    .append(cardToString[accusationLogic.get(i++)]).append(" ")
                    .append(cardToString[accusationLogic.get(i++)]).append(") ");
        }
        result.append("\n");

        return result.toString();
    }

}
