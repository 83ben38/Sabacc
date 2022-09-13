import acm.program.GraphicsProgram;
import svu.csc213.Dialog;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Sabacc extends GraphicsProgram {
    public GCard topCard;
    public Deck deck;
    public Hand[] hands;
    public static int wins = 0;
    public static int losses = 0;
    public Hand discard;
    public boolean pas = false;
    public static GCard currentClicked;
    public static void main(String[] args) {
        new Sabacc().start();
    }
    @Override
    public void init(){
        int length = Dialog.getInteger("How many opponents (1-3)") + 1;
        while (length > 4 || length < 2){
            length = Dialog.getInteger("How many opponents (1-3)") + 1;
        }
        hands = new Hand[length];
        JButton pass = new JButton("Pass");
        add(pass,SOUTH);
        pass.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pas = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        while (getWidth() < 1000){
            pause(1);
        }
    }
    @Override
    public void run() {
        for (int i = 0; i < hands.length; i++) {
            hands[i] = new Hand();
        }
        discard = new Hand();
        currentClicked = null;
        deck = new Deck();
        topCard = deck.drawCard();
        add(topCard,getWidth()/2-topCard.getWidth()/2,getHeight()/2-topCard.getHeight()/2);
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < hands.length; i++) {
                double x;
                if (i%2==0){
                    x = ((j-2.5) * topCard.getWidth()) + getWidth()/2;
                }
                else{
                    if (i == 1){
                        x = 0;
                    }
                    else{
                        x = getWidth()- topCard.getWidth();
                    }
                }
                double y;
                if (i%2==1){
                    y = ((j-2.5) * topCard.getHeight()) + getHeight()/2;
                }
                else{
                    if (i == 2){
                        y = 0;
                    }
                    else{
                        y = getHeight()- topCard.getHeight();
                    }
                }
                int I = i;
                new Thread(() -> deal(x, y, I == 0, hands[I])).start();
                pause(250);
            }
        }
        deal(getWidth()/2+topCard.getWidth()/2,getHeight()/2-topCard.getHeight()/2,false,discard);
        discard.getTopCard().flip(false);
        pas = false;
        for (int i = 0; i < 3; i++) {
            while (true) {
                while (currentClicked == null && !pas) {
                    pause(1);
                }
                if (pas){
                    break;
                }
                if (currentClicked.isFaceUp()){
                    break;
                }
                if (currentClicked == topCard){
                    break;
                }
            }
            if (!pas) {
                if (currentClicked == topCard) {
                    deal(((hands[0].getSize() - 2.5) * topCard.getWidth()) + getWidth() / 2, getHeight() - topCard.getHeight(), true, hands[0]);
                } else if (currentClicked == discard.getTopCard()) {
                    currentClicked = null;
                    while (true) {
                        while (currentClicked == null) {
                            pause(1);
                        }
                        if (currentClicked.isFaceUp() && currentClicked != discard.getTopCard()){
                            break;
                        }
                    }
                    takeDiscard(((hands[0].getSize() - 2.5) * topCard.getWidth()) + getWidth() / 2, getHeight() - topCard.getHeight(), true, hands[0]);
                    int cardNumber = hands[0].cardNumber(currentClicked);
                    discard(hands[0].discard(cardNumber));
                    hands[0].moveCards(cardNumber, (int) -topCard.getWidth(), 0);

                } else {
                    int cardNumber = hands[0].cardNumber(currentClicked);
                    discard(hands[0].discard(cardNumber));
                    hands[0].moveCards(cardNumber, (int) -topCard.getWidth(), 0);
                    deal(((hands[0].getSize() - 2.5) * topCard.getWidth()) + getWidth() / 2, getHeight() - topCard.getHeight(), true, hands[0]);
                }
            }
            for (int j = 1; j < hands.length; j++) {
                pas = false;
                currentClicked = decideAction(j);
                if (!pas) {
                    if (currentClicked == topCard) {
                        deal(j);
                    } else if (currentClicked == discard.getTopCard()) {
                        currentClicked = decideSwitch(j);
                        takeDiscard(j);
                        int cardNumber = hands[j].cardNumber(currentClicked);
                        discard(hands[j].discard(cardNumber));
                        hands[j].moveCards(cardNumber, j == 2 ? -(int)topCard.getWidth() : 0, j != 2 ? -(int)topCard.getHeight() : 0);

                    } else {
                        int cardNumber = hands[j].cardNumber(currentClicked);
                        discard(hands[j].discard(cardNumber));
                        hands[j].moveCards(cardNumber, j == 2 ? -(int)topCard.getWidth() : 0, j != 2 ? -(int)topCard.getHeight() : 0);
                        deal(j);
                    }
                }
            }

            currentClicked = null;
            pas = false;
        }
        for (Hand cards: hands) {
            for (int i = 0; i < cards.getSize(); i++) {
                if (!cards.getCard(i).isFaceUp()){
                    cards.getCard(i).flip(false);
                }
            }
        }
        pause (2500);
        restart();
    }
    public void discard(GCard card){
        double[] moveAmount = new double[]{(getWidth()/2+topCard.getWidth()/2 - card.getX())/100, (getHeight()/2-topCard.getHeight()/2-card.getY())/100};
        for (int i = 0; i < 100; i++) {
            card.move(moveAmount[0],moveAmount[1]);
            card.sendToFront();
            pause(10);
        }
        if (!card.isFaceUp()){
            card.flip(false);
        }
        discard.addCard(card,false);
    }
    public void deal(double x, double y, boolean self, Hand hand){
        GCard movingCard = topCard;
        double[] moveAmount = new double[]{(x - movingCard.getX())/100, (y-movingCard.getY())/100};
        topCard = deck.drawCard();
        add(topCard,getWidth()/2-topCard.getWidth()/2,getHeight()/2-topCard.getHeight()/2);
        for (int i = 0; i < 100; i++) {
            movingCard.move(moveAmount[0],moveAmount[1]);
            pause(10);
        }
        if (self){
            movingCard.flip(true);
        }
        hand.addCard(movingCard,self);
    }
    public void takeDiscard(double x, double y, boolean self, Hand hand){
        GCard movingCard = discard.discard(discard.cardNumber(discard.getTopCard()));
        double[] moveAmount = new double[]{(x - movingCard.getX())/100, (y-movingCard.getY())/100};
        for (int i = 0; i < 100; i++) {
            movingCard.move(moveAmount[0],moveAmount[1]);
            pause(10);
        }
        if (!self){
            movingCard.flip(false);
        }
        hand.addCard(movingCard,self);
    }
    public GCard decideAction(int hand){
        Hand theHand = hands[hand];
        int currentScore = (theHand.getValue());
        if (currentScore == 0) {
            pas = true;
        }
        int topCardValue = currentScore + topCard.getValue();
        int discardValue = currentScore + discard.getTopCard().getValue();
        for (int i = 0; i < theHand.getSize(); i++) {
            if (Math.abs(discardValue - theHand.getValue(i)) < Math.abs(currentScore) && Math.abs(discardValue - theHand.getValue(i)) <  Math.abs(topCardValue)){
                return discard.getTopCard();
            }
        }
        for (int i = 0; i < theHand.getSize(); i++) {
            if (Math.abs(topCardValue - theHand.getValue(i)) < Math.abs(currentScore) && Math.abs(topCardValue - theHand.getValue(i)) < Math.abs(topCardValue)){
                return theHand.getCard(i);
            }
        }
        if (Math.abs(topCardValue) < Math.abs(currentScore)){
            return topCard;
        }
        else{
            pas = true;
        }
        return null;
    }
    public GCard decideSwitch(int hand){
        Hand theHand = hands[hand];
        int currentScore = theHand.getValue() + discard.getTopCard().getValue();
        int discardValue = currentScore - theHand.getValue(0);
        int bestCard = 0;
        for (int i = 1; i < theHand.getSize(); i++) {
            if (Math.abs(currentScore - theHand.getValue(i)) < Math.abs(discardValue)){
                discardValue = currentScore - theHand.getValue(i);
                bestCard = i;
            }
        }
        return theHand.getCard(bestCard);
    }
    public void restart(){
        Hand selfHand = hands[0];
        Arrays.sort(hands, (o1, o2) -> o1.isBetterThan(o2) ? -1:1);
        Dialog.showMessage("You " + (selfHand == hands[0] ? "WIN!" : "lose..."));
        if (selfHand == hands[0]){
            Dialog.showMessage("You beat the opponents score of " + hands[1].getValue() + " with your score of " + selfHand.getValue() + ".");
            wins++;
        }
        else{
            Dialog.showMessage("You lost to the opponents score of "+ hands[0].getValue()+ " with your score of " + selfHand.getValue() + ".");
            losses++;
        }
        removeAll();
        if (Dialog.getYesOrNo("Would you like to play again?")){
            run();
        }
        else {
            Dialog.showMessage("Your win rate is " + ((wins/(wins+losses))*100) + "%.");
            exit();
        }
    }
    public void deal(int i){
        double x;
        int j = hands[i].getSize();
        if (i%2==0){
            x = ((j-2.5) * topCard.getWidth()) + getWidth()/2;
        }
        else{
            if (i == 1){
                x = 0;
            }
            else{
                x = getWidth()- topCard.getWidth();
            }
        }
        double y;
        if (i%2==1){
            y = ((j-2.5) * topCard.getHeight()) + getHeight()/2;
        }
        else{
            if (i == 2){
                y = 0;
            }
            else{
                y = getHeight()- topCard.getHeight();
            }
        }
        deal(x,y, false, hands[i]);
    }
    public void takeDiscard(int i){
        double x;
        int j = hands[i].getSize();
        if (i%2==0){
            x = ((j-2.5) * topCard.getWidth()) + getWidth()/2;
        }
        else{
            if (i == 1){
                x = 0;
            }
            else{
                x = getWidth()- topCard.getWidth();
            }
        }
        double y;
        if (i%2==1){
            y = ((j-2.5) * topCard.getHeight()) + getHeight()/2;
        }
        else{
            if (i == 2){
                y = 0;
            }
            else{
                y = getHeight()- topCard.getHeight();
            }
        }
        takeDiscard(x,y, false, hands[i]);
    }

}
