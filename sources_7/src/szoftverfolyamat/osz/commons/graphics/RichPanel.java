package szoftverfolyamat.osz.commons.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/*
Az aranyos nagyithatosagot es kicsinyithetoseget
biztostja, egyeb kiegeszito funkciok mellett.
*/
public class RichPanel extends GenericPanel {
    private HashSet<Entry> entries;

    public RichPanel() {
        super();
        setNullLayout();
        entries = new HashSet<Entry>();
    }

    public RichPanel(int s) {
        super(s);
    }
    
    /*
    A fix meretet adja meg  x  es  y.
    */
    public RichPanel(int x, int y) {
        super();
        setAllSizes(x, y);
        setNullLayout();
        entries = new HashSet<Entry>();
    }

    /*
    Abszolut atmeretezes.
    */
    public void setAllSizes(int x, int y) {
        setSize(x, y);
        setMaximumSize(new Dimension(x, y));
        setMinimumSize(new Dimension(x, y));
        setPreferredSize(new Dimension(x, y));
    }

    public void setSize(int w, int h) {
        super.setSize(w, h);
    }

    public void forceRealign() {
        setSize(getWidth(), getHeight());
    }

    /*
    A panel szelesseg/magassag aranya.
    */
    public double getRectangleRatio() {
        return (double)getWidth()/(double)getHeight();
    }

    /*
    Ez a metodus egy olyan belso komponenst ad ehhez a penelhez amely e panel
    atmeretezesekor szinten, aranyosan atmeretezodik. Az aranyt a parameterek
    hatarozzak meg:    
    p   :  a hozzadni kivant komponens
    hr  :  vizszintes arany 0..1 tartomanyban, mekkora legyek a child szelessege
           a parentehez kepest
    vr  :  fuggologes arany 0..1 tartomanyban, mekkora legyek a child magassaga
           a parentehez kepest           
    rox :  a child bal felso vizszintes koordinatajanak aranya a teljes parent 
           panelhez kepest a 0..1 tartomanyban
    roy :  a child bal felso fuggoleges koordinatajanak aranya a teljes parent 
           panelhez kepest a 0..1 tartomanyban           
    */
    public void addProportionalChild(Component p, double hr, double vr,
                                     double rox, double roy) {
        synchronized (entries) {
            entries.add(new Entry(p, hr, vr, rox, roy));
            add(p, 0);
            validate();
        }
    }

    /*
    Az elozo metodussal regisztralt kopmonenseket aranyosan atmeretezi, hogy
    a szelesseg/magassag/elhelyezkedes aranyuk megmeradjon. Kozben ez a panel
    is atmeretezodik a w (szelesseg) es h (magassag) parameterek szerint.
    */
    public void proportionalResize(int w, int h) {
        super.setSize(w, h);
        Iterator<Entry> i = entries.iterator();
        while(i.hasNext()) {
            Entry e = i.next();
            e.panel.setSize((int)((double)w*e.horizontalRatio),
                            (int)((double)h*e.verticalRatio));
            e.panel.setLocation((int)((double)w*e.relativeOrigoX),
                                (int)((double)h*e.relativeOrigoY));
        }
    }

    // nincs impl.
    public void proportionalMove(double dw, double dh) {}

    /*
    Aranyosan kezelt komponensek eltavolitasa a panelrol.
    */
    public void removeProportionalChildren() {
        synchronized (entries) {
            Iterator<Entry> i = entries.iterator();
            while(i.hasNext()) {
                Entry e = i.next();
                remove(e.panel);
            }
            revalidate();
            entries = new HashSet<Entry>();
        }
    }

    /*
    Aranyosan kezelt c komponens eltavolitasa a panelrol.
    */
    public void removeProportionalChild(Component c) {
        synchronized (entries) {
            Iterator<Entry> i = entries.iterator();
            while(i.hasNext()) {
                Entry e = i.next();
                if(e.panel==c) {
                    remove(c);
                    entries.remove(e);
                    revalidate();
                    break;
                }
            }
        }
    }

    /*
    Aranyosan kezelt komponensek listazasa.
    */
    public Component[] getProportionalChildren() {
        Iterator<Entry> i = entries.iterator();
        Vector<Component> v = new Vector<Component>();
        while(i.hasNext()) {
            Entry e = i.next();
            v.add(e.panel);
        }
        return v.toArray(new Component[0]);
    }

    /*
    Egy komponens-bejegyzes
    */
    protected class Entry {
        public Component panel;
        public double horizontalRatio;
        public double verticalRatio;
        public double relativeOrigoX;
        public double relativeOrigoY;

        public Entry(Component p, double hr, double vr,
                     double rox, double roy) {
            panel = p;
            horizontalRatio = hr;
            verticalRatio = vr;
            relativeOrigoX = rox;
            relativeOrigoY = roy;
        }
    }
}