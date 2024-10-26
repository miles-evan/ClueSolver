

public class Main {
    public static void main(String[] args) {
        ClueLogic logic = new ClueLogic();
        logic.add(1, 0, 6, 12);
        logic.add(1, 1, 7, 13);
        logic.addAccusation(1, 2, 3);
        logic.print();
    }

}
