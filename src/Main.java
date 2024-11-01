import java.io.File;
import java.util.Scanner;

public class Main {
    private static void noArgs() {
        test1();
    }


    private static void test4() {
        Hands hands = new Hands(3, 6, 6, 6);
        System.out.println(hands);
        for(int i = 0; i <= 8; i++) {
            hands.setValue(0, i, false);
            System.out.println(hands);
            hands.setValue(1, i, false);
            System.out.println(hands);
        }
        for(int i = 9; i <= 13; i++) {
            System.out.println("next up " + 0 + " " + i);
            System.out.println(hands.setValue(0, i, true));
            System.out.println(hands);
        }
        System.out.println("now using a backtracker");
        ClueBackTracker backTracker = new ClueBackTracker(3, -1, hands, new ClueLogic(3));
        //System.out.println(backTracker.completePossible(hands, 0, 14, true));
    }
    private static void test3() {
        Hands hands = new Hands(3, 6, 6, 6);
        System.out.println(hands);
        for(int i = 0; i <= 8; i++) {
            hands.setValue(0, i, false);
            System.out.println(hands);
            hands.setValue(1, i, false);
            System.out.println(hands);
        }
        for(int i = 9; i <= 14; i++) {
            System.out.println("next up " + 0 + " " + i);
            System.out.println(hands.setValue(0, i, true));
            System.out.println(hands);
        }
    }
    private static void test2() {
        Hands hands = new Hands(3, 6, 6, 6);
        hands.setCheck(2, 0);
        hands.setCheck(2, 1);
        hands.setCheck(2, 2);
        hands.setCheck(2, 3);
        hands.setCheck(2, 4);
        System.out.println(hands);
    }

    private static void test1() {
        Clue clue = new Clue(3, 6, 6, 6);
        clue.setTimeLimit(-1);

        for(int i = 0; i < 8; i++) {
            clue.setX(0, i);
            clue.setX(1, i);
        }
        clue.setX(0, 8);
        clue.print();
        clue.update();
        clue.print();//*/

        /*for(int i = 0; i < 8; i++) {
            clue.setX(0, i);
            clue.setX(1, i);
        }
        System.out.println(clue.setCheck(0, 9));
        System.out.println(clue.setCheck(0, 10));
        System.out.println(clue.setCheck(0, 11));
        System.out.println(clue.setCheck(0, 12));
        System.out.println(clue.setCheck(0, 13));
        System.out.println(clue.setCheck(0, 14));
        System.out.println(clue.setCheck(1, 15));
        System.out.println(clue.setCheck(1, 16));
        System.out.println(clue.setCheck(1, 17));
        System.out.println(clue.setCheck(1, 18));
        System.out.println(clue.setCheck(1, 19));
        clue.print();//*/
    }


    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            noArgs();
        } else if(args[0].equals("play")) {
            if(args.length == 2) play(Integer.parseInt(args[1]));
            else play(args[1], Integer.parseInt(args[2]));
        }
    }


    private static void play(int timeLimit) {
        ClueGame clueGame = new ClueGame();
        clueGame.setTimeLimit(timeLimit);
        clueGame.play();
    }
    private static void play(String filename, int timeLimit) {
        ClueGame clueGame = new ClueGame();
        clueGame.setInputSource(filename);
        clueGame.setTimeLimit(timeLimit);
        clueGame.play();
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
}
