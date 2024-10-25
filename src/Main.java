

public class Main {
    public static void main(String[] args) {
        Clue clue = new Clue();
        clue.setCheck(2, 1);
        clue.setCheck(2, 7);
        clue.addInfo(1, 0, 6, 12, 1);

        clue.print();

    }

}
