

public class Main {
    public static void main(String[] args) {
        Clue clue = new Clue();
        clue.setTimeLimit(-1);
        for(int i = 0; i < 15; i++) {
            clue.setX(0, i);
            clue.setX(1, i);
        }
        clue.setX(0, 15);
        clue.print();
        clue.update();
        clue.print();
    }

}
