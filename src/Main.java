import java.io.File;
import java.util.Scanner;
/**
 You can ignore this class, this is just for me.
 This is the entry point to the program.
 */
public class Main {
    private static void noArgs() {
        ClueGame clueGame = new ClueGame();
        clueGame.setTimeLimit(-1);
        clueGame.setNumAssumptions(0);
        clueGame.play();
    }


    public static void main(String[] args) {
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
