package szoftverfolyamat.osz.commons.graphics;

import java.awt.Color;

/* 
Ugyanaz mint az  AbPanel  de a magassag/szelesseg aranyat fixen tartja
*/
public class AbProportionalPanel extends AbPanel {
    protected double ratio;
    
    /*
    Fix szelesseg/magassag  r  arany.
    */
    public AbProportionalPanel(double r) {
        super();
        ratio = r;
    }
    
    /*
    Fix szelesseg/magassag  r  arany plusz a panel szine.
    */    
    public AbProportionalPanel(double r, Color c) {
        this(r);
        setBackground(c);
        setOpaque(true);
    }
    
    public AbProportionalPanel(double r, int c) {
        this(r, new Color(c));
    }    
    
    public void setWidth(int w) {
        super.setSize(w, (int)(w/ratio));
    }

    public void setHeight(int h) {
        super.setSize((int)(h*ratio), h);
    }    
    
    public void setSize(int w, int h) {
        setWidth(w);
    }  
    
    protected double getRatio() {
        return ratio;
    }
}
