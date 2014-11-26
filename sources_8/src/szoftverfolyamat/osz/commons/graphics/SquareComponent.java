package szoftverfolyamat.osz.commons.graphics;

/*
Negyzet alaku komponens.
*/
public abstract class SquareComponent extends AbProportionalPanel {
    private double side;

    public SquareComponent() {
        super(1);
    }

    public void setSide(double s) {
        side = s;
        setSize((int)s, (int)s);
    }

    public double getSide() {
        return side;
    }

    public void setSize(int w, int h) {
        super.setWidth(w);
        side = w;
    }
}
