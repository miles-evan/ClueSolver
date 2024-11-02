import java.io.File;
import java.util.Scanner;

public class Main {
    private static void noArgs() {
        System.out.println("no args");
        ClueGame clueGame = new ClueGame(4, 5, 5, 4, 4);
        clueGame.setTimeLimit(-1);
        clueGame.setInputSource("data/sampleMoves2.txt");
        clueGame.play();
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
}
