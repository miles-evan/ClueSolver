import java.util.Scanner;

public class ClueGame {
    private final Clue clue;
    private final int n;
    private int turn = 0;
    private int round = 1;
    private boolean[] out;

    public ClueGame(int n, int ... handSizes) {
        this.n = n;
        clue = new Clue(n, handSizes);
        out = new boolean[n];
    }
    public ClueGame() {
        n = 6;
        clue = new Clue();
        out = new boolean[n];
    }

    private void next() {
        turn = (turn + 1) % n;
        if(turn == 0) round ++;
        if(out[turn]) next();
    }

    public void turn(int weapon, int suspect, int room, int numTries) {
        clue.addInfo(turn, weapon, suspect, room, numTries);
    }
    public void turn(int weapon, int suspect, int room, int numTries, int cardHandedOver) {
        clue.addInfo(turn, weapon, suspect, room, numTries, cardHandedOver);
    }

    public void accuse(int player, int weapon, int suspect, int room) {
        out[player] = true;
        clue.incorrectAccusation(suspect, weapon, room);
    }



    public void play() {
        Scanner in = new Scanner(System.in);
        System.out.println("Player# cardsInHand...");
        String[] playersCards = in.nextLine().split(" ");
        for(int i = 1; i < playersCards.length; i ++) {
            clue.setCheck(Integer.parseInt(playersCards[0]), Integer.parseInt(playersCards[i]));
        }

        while(true) {
            System.out.println("\n\n\n");
            print(true);
            System.out.println("suspect weapon room numTries [cardHandedOver]");
            String input = in.nextLine();
            String[] splitInput = input.split(" ");
            if (splitInput.length <= 1) break;
            if(splitInput.length == 3) {
                accuse(turn, Integer.parseInt(splitInput[0]),
                        Integer.parseInt(splitInput[1]),
                        Integer.parseInt(splitInput[2]));
            } else if (splitInput.length == 4) {
                turn(
                        Integer.parseInt(splitInput[0]),
                        Integer.parseInt(splitInput[1]),
                        Integer.parseInt(splitInput[2]),
                        Integer.parseInt(splitInput[3])
                );
            } else if (splitInput.length == 5) {
                turn(
                        Integer.parseInt(splitInput[0]),
                        Integer.parseInt(splitInput[1]),
                        Integer.parseInt(splitInput[2]),
                        Integer.parseInt(splitInput[3]),
                        Integer.parseInt(splitInput[4])
                );
            }
            next();
        }

        clue.printDifference();

    }


    public void print(boolean printPrimitive) {
        if(printPrimitive) clue.printPrimitiveTable();
        clue.print();
        System.out.println("(Round " + round + ")");
        System.out.println("It is now player " + turn + "'s turn");
    }


    public static void main(String[] args) {
        ClueGame clueGame = new ClueGame();
        clueGame.play();
    }

}
