

public class Main {
    public static void main(String[] args) {
        ClueGame clueGame = new ClueGame();
        clueGame.setTimeLimit(-1);
        clueGame.play();
    }

    private static void completeBackTrackTest() {
        Clue clue = new Clue();
        clue.setTimeLimit(-1);
        for(int i = 0; i < 15; i++) {
            clue.setX(0, i);
            clue.setX(1, i);
        }
        clue.setX(0, 15);

        /*
        clue.setCheck(2, 0);
        clue.setCheck(2, 1);
        clue.setCheck(2, 2);
        clue.setCheck(3, 3);
        clue.setCheck(3, 4);
        clue.setCheck(3, 6);
         */

        clue.print();
        clue.update();
        clue.print();
    }

}
