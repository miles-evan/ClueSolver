import java.util.ArrayList;

public class ClueLogic {
    private final int n;
    private final ArrayList<Integer>[] logic;
    private int numLogicVars = 0;
    private final ArrayList<Integer> accusationLogic;

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

    public void add(int player, int suspect, int weapon, int room) {
        logic[player].add(suspect);
        logic[player].add(weapon);
        logic[player].add(room);
        numLogicVars += 3;
    }

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

    public int getAccusation(int index) {
        return accusationLogic.get(index);
    }
    public int getPlayer(int index) {
        return get(0, index)[0];
    }
    public int getCard(int index) {
        return get(0, index)[1];
    }
    private int[] get(int player, int index) {
        if(index >= logic[player].size())
            return get(player + 1, index - logic[player].size());
        return new int[] {player, logic[player].get(index)};
    }

    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("-LOGIC-\n");
        for (int i = 0; i < n; i++) {
            result.append("Player ").append(i).append(": ");
            for (int j = 0; j < logic[i].size();) {
                result.append("(")
                        .append(logic[i].get(j++)).append(" ")
                        .append(logic[i].get(j++)).append(" ")
                        .append(logic[i].get(j++)).append(") ");
            }
            result.append("\n");
        }

        result.append("Accusations: ");
        for (int i = 0; i < accusationLogic.size(); i += 3) {
            result.append("(")
                    .append(accusationLogic.get(i++)).append(" ")
                    .append(accusationLogic.get(i++)).append(" ")
                    .append(accusationLogic.get(i++)).append(") ");
        }
        result.append("\n");

        return result.toString();
    }

}
