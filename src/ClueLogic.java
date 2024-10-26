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
}
