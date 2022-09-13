import java.util.ArrayList;

public class Deck {
    ArrayList<GCard> cards = new ArrayList<>();
    public Deck(){
        char[] suites = new char[]{'S','C','T'};
        for (char suit: suites) {
            for (int i = -10; i <= 10; i++) {
                if (i != 0) {
                    if (i > 0) {
                        cards.add(new GCard(suit + "+" + i));
                    }
                    else{
                        cards.add(new GCard(suit + "" + i));
                    }
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            cards.add(new GCard("0"));
        }
    }
    public GCard drawCard(){
        int cardNumber = randomInt(0,cards.size()-1);
        GCard returner = cards.get(cardNumber);
        cards.remove(cardNumber);
        return returner;
    }
    public static int randomInt(int from, int to){
        return (int) (Math.random() * (to-from+1)) + from;
    }
}
