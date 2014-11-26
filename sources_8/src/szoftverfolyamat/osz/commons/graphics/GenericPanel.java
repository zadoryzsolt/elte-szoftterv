package szoftverfolyamat.osz.commons.graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;

public class GenericPanel extends JPanel {
    public GenericPanel() {
        super();
        setOpaque(true);
    }
    
    public GenericPanel(int c) {
        super();
        setBackground(new Color(c));
        setOpaque(true);
    }

    public void setNullLayout() {
        setLayout(null);
    }

    public void setBorderLayout() {
        setLayout(new BorderLayout());
    }

    public void setFlowLayout() {
        setLayout(new FlowLayout());
    }   
    
    public void setBoxLayout(boolean b) {
        if(b) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        } else {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }
    }

    public void setBackground(int c) {
        setBackground(new Color(c));
    }
    
    /*
    Feliratos keretet ad a panelnek.
    */    
    public void setTitledBorder(String t, int c) {
        Border bline =
            BorderFactory.createLineBorder(new Color(c));
        TitledBorder title = BorderFactory.createTitledBorder(bline, t);
        title.setTitleJustification(TitledBorder.LEFT);
        setBorder(title);
    }

    // ures keret
    public void setEmptyBorder(int w) {
        setBorder(new EmptyBorder(w, w, w, w));
    }
    
    /*
    Keretet ad a panelnek.
    */    
    public void setLinedBorder(Color c, int w) {
        Border lined = BorderFactory.createLineBorder(c, w);
        setBorder(lined);
    }
    
    public void setAllSizes(int x, int y) {
        setSize(x, y);
        setMaximumSize(new Dimension(x, y));
        setMinimumSize(new Dimension(x, y));
        setPreferredSize(new Dimension(x, y));
    }    
    
    public void addToStart(Component c) {
        add(c, BorderLayout.PAGE_START);
    }

    public void addToEnd(Component c) {
        add(c, BorderLayout.PAGE_END);
    }

    public void addToTop(Component c) {
        addToStart(c);
    }

    public void addToBottom(Component c) {
        addToEnd(c);
    }

    public void addToCenter(Component c) {
        add(c, BorderLayout.CENTER);
    }

    public void addToLeft(Component c) {
        add(c, BorderLayout.WEST);
    }

    public void addToRight(Component c) {
        add(c, BorderLayout.EAST);
    }    
    
    public void invokeLater(Runnable r) {
        SwingUtilities.invokeLater(r);
    }
}