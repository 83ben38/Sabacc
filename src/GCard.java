import acm.graphics.GCompound;
import acm.graphics.GImage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GCard extends GCompound {
    private Card card;
    private GImage image;
    private GImage back;
    public GCard(String s, boolean faceUp) {
        image = new GImage("./Cards/" + s + ".png");
        back = new GImage("./Cards/back.png");
        if (faceUp){
            add(image);
        }
        else{
            add(back);
        }
        //e.g. Y2 (ACE = 14)
        if (s.length() > 1){
            s = s.substring(1);
        }
        card = new Card(Card.Face.getFace(Integer.parseInt(s)), faceUp);
        image.scale(0.2);
        back.scale(0.2);
        GCard THIS = this;
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Sabacc.currentClicked = THIS;
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
    }
    public GCard(String s){
        this(s,false);
    }
    public GCard flip(boolean print){
        card.flip();
        removeAll();
        if (card.isFaceUp()){
            add(image);
            if (print){
                System.out.println("You drew a " + card.toString());
            }
        }
        else{
            add(back);
        }
        return this;
    }
    public boolean isFaceUp(){
        return card.isFaceUp();
    }
    public int getValue(){
        return card.getValue();
    }
}
