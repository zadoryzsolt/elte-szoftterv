package szoftverfolyamat.osz.commons.graphics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Color;

public class GenericLabel extends JLabel {
    public GenericLabel(int col, String cap) {
        super();
        setForeground(new Color(col));
        setText(cap);
        setOpaque(false);
    }
    
    public GenericLabel(String cap) {
        super(cap);
        setOpaque(false);
    }
    
    public GenericLabel(ImageIcon i) {
        super(i);
    }

    public void setForeground(int c) {
        setForeground(new Color(c));
    }

    public void alignToCenter() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void alignToLeft() {
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    public void alignToRight() {
        setHorizontalAlignment(SwingConstants.RIGHT);
    }
}