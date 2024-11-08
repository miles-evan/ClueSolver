import clueSolvingAlgorithm.ClueGame;

/**
 This is the entry point to the program.
 You can use this to try out my algorithm.
 */
public class Main {
    private static void noArgs() {
        play("sampleMoves1.txt");
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
    private static void play() {play(-1);}
    private static void play(String filename) {play(filename, -1);}
}
