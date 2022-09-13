public class Card {
    public enum Face{
        NEGATIVE_TEN, NEGATIVE_NINE, NEGATIVE_EIGHT, NEGATIVE_SEVEN, NEGATIVE_SIX, NEGATIVE_FIVE, NEGATIVE_FOUR, NEGATIVE_THREE, NEGATIVE_TWO, NEGATIVE_ONE, ZERO, POSITIVE_ONE, POSITIVE_TWO, POSITIVE_THREE, POSITIVE_FOUR, POSITIVE_FIVE, POSITIVE_SIX, POSITIVE_SEVEN, POSITIVE_EIGHT, POSITIVE_NINE, POSITIVE_TEN;
        public static Face getFace(int faceNumber){
            for (Face face: values()) {
                if (face.ordinal() == faceNumber + 10){
                    return face;
                }
            }
            return null;
        }
    }
    private final Face FACE;
    private boolean isFaceUp;

    public Card(Face face, boolean isFaceUp){
        FACE = face;
        this.isFaceUp = isFaceUp;
    }

    public int getValue(){
        return FACE.ordinal() - 10;
    }
    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void flip(){
        isFaceUp = !isFaceUp;
    }

    @Override
    public String toString() {
        return FACE.name().replace("_"," ");
    }
}
