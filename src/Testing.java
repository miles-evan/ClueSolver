/**
    You can ignore this class, this is just for me.
    I should really turn these into unit tests...
 */
public class Testing {
    public static void main(String[] args) {
        completeBacktrackTest();
    }

    private static void completeBacktrackTest() {
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

    private static void completeBacktrackTest2() {
        Clue clue = new Clue();
        clue.setTimeLimit(-1);
        for(int i = 0; i < 12; i++) {
            clue.setX(0, i);
            clue.setX(1, i);
        }
        clue.setX(1, 12);
        clue.setX(0, 16);
        clue.setX(0, 17);
        for(int i = 18; i < 21; i++) {
            clue.setX(0, i);
            clue.setX(1, i);
        }

        clue.print();
        clue.update();
        clue.print();
    }

    private static void completeBacktrackTest3() {
        Clue clue = new Clue(3, 6, 6, 6);
        clue.setTimeLimit(-1);

        for(int i = 0; i < 8; i++) {
            clue.setX(0, i);
            clue.setX(1, i);
        }
        clue.setX(0, 8);
        clue.print();
        clue.update();
        clue.print();
    }
}
