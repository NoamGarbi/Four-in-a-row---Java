public class Spot {
    private Type color;

    //Initialize the spots
    public Spot() {
        this.color = Type.WHITE;
    }

    public Type getColor() {
        return this.color;
    }
    public void setColor(Type color) {
        this.color = color;
    }
    public boolean isWhite() {
        return this.color == Type.WHITE;
    }
}
