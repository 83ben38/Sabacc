import java.util.ArrayList;

import static acm.util.JTFTools.pause;

public class Hand {
    private ArrayList<GCard> cards = new ArrayList<>();
    public void addCard(GCard card, boolean self){
        cards.add(card);
        if (self) {
            System.out.println("The score is now " + getValue() + ".");
        }
    }
    public void moveCards(int move, int x, int y){
        for (int i = 0; i < 100; i++) {
            for (int j = move; j < cards.size(); j++) {
                cards.get(j).move((double)x/100,(double)y/100);
                pause(3);
            }
        }

    }
    public GCard getCard(int i){
        return cards.get(i);
    }
    public GCard getTopCard(){
        return cards.get(cards.size()-1);
    }
    public GCard discard(int card){
        GCard returner = cards.get(card);
        cards.remove(card);
        return returner;
    }
    public int cardNumber(GCard card){
        return cards.indexOf(card);
    }
    public int getValue(int card){
        return cards.get(card).getValue();
    }
    public int getValue(){
        int value = 0;
        for (GCard card: cards) {
            value += card.getValue();
        }
        return value;
    }
    public int getSize(){
        return cards.size();
    }
    public boolean isBetterThan(Hand hand){
        if (Math.abs(getValue()) < Math.abs(hand.getValue()) || Math.abs(getValue()) > Math.abs(hand.getValue())){
            return Math.abs(getValue()) < Math.abs(hand.getValue());
        }
        else if (getValue() != hand.getValue()){
            return getValue() > hand.getValue();
        }
        else{
            int zeros = 0;
            for (GCard card: cards) {
                if (card.getValue() == 0){
                    zeros++;
                }
            }
            for (GCard card: hand.cards) {
                if (card.getValue() == 0){
                    zeros--;
                }
            }
            if (zeros != 0){
                return zeros > 0;
            }
            if (hand.getValue() == 0){
                return cards.size() < hand.cards.size();
            }
            else{
                return cards.size() > hand.cards.size();
            }
        }
    }
}
