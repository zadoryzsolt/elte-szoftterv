package szoftverfolyamat.osz.commons.graphics;

import java.awt.Graphics;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

/*
Altalanos absztrakt grafikai komponens, int allapotokkal, atlatszosaggal, 
igazitasi konstansokkal (L/C/R)
*/
public abstract class AbComponent extends RichPanel {
    public final static int CENTERED = 0;
    public final static int LEFT = 1;
    public final static int RIGHT = 2;
    
    private int state;
    private float transparency;

    public AbComponent() {
        super();
        transparency = 1f;
    }

    protected void setState(int i) {
        state = i;
    }

    protected int getState() {
        return state;
    }

    protected boolean isState(int t) {
        return state==t;
    }
    
    public void setTransparency(float f) {
        transparency = f;
    }
    
    public void paint(Graphics g) {
        if(transparency!=1f) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            super.paint(g2);
            g2.dispose();
        } else {
            super.paint(g);
        }
    }
}

