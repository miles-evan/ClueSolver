import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length == 0) {


            System.out.println("nothing");


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
