

public class Main {
    public static void main(String[] args) {
        Hands hands = new Hands();
        for(int i = 0; i < 17; i ++) {
            System.out.println(hands.setX(1, i));
            System.out.println(hands.setX(2, i));
        }
        System.out.println(hands.setX(1, 17));
        System.out.println(hands);

    }

}
