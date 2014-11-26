package szoftverfolyamat.osz.commons.graphics;

import java.awt.Color;

/*
Alapveto osztaly; (szinte) minden grafikus komponens erre a 
panelre kerul.
*/
public class AbPanel extends AbComponent {   
    protected boolean repaintsPar;
    
    /*
    Atlatszo panel, "placeholder" celbol.
    */
    public AbPanel() {
        super();
        repaintsPar = false;
        setOpaque(false);   // nem latszik
    }

    /*
    Latszo,  c  hatterszinu panel.
    */
    public AbPanel(Color c) {
        super();
        setOpaque(true);  // tehat   L A T S Z I K
        setBackground(c);
        repaintsPar = false;
    }
    
    public AbPanel(int t) {
        this(new Color(t));
    }
    
    public void setSize(int w, int h) {
        proportionalResize(w, h);
    }
    
    public void setLocation(int dw, int dh) {
        super.setLocation(dw, dh);
    }
    
    /*
    Ha  true  akkor frissiteskor a parent-jet is ujra festi, 
    kulonben nem.
    */
    public void repaintsParent(boolean b) {
        repaintsPar = b;
    }
    
    public void repaint() {
        if((repaintsPar)&&(getParent()!=null)) {
            getParent().repaint();
        }
        super.repaint();
    }
}



