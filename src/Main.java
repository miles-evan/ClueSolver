

public class Main {
    public static void main(String[] args) {
        Clue clue = new Clue();

        clue.setCheck(6, 6);
        clue.print();
        clue.setCheck(6, 12);
        clue.print();
        clue.incorrectAccusation(0, 6, 12);
        clue.print();

    }

}
