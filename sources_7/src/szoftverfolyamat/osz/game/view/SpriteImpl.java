package szoftverfolyamat.osz.game.view;

import szoftverfolyamat.osz.commons.graphics.GenericLabel;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JViewport;

public class SpriteImpl extends JLabel implements Sprite {
    private final ImageIcon[] icons;

    public SpriteImpl(ImageIcon[] ia) {
        super(ia[0]);
        icons = new ImageIcon[ia.length];
        for(int i=0; i<ia.length; i++) {
            icons[i] = ia[i];
        }
        setImage(0);
    }

    @Override
    public void moveTo(double x, double y) {
        setBounds((int)x, (int)y, getWidth(), getHeight());
    }

    @Override
    public void moveBy(double dx, double dy) {
        double x = getLocation().getX();
        double y = getLocation().getY();
        moveTo(x+dx, y+dy);
        //setBounds((int)(x+dx), (int)(y+dy), getWidth(), getHeight());
    }

    @Override
    public void setImage(int i) {
        setIcon(icons[i]);
        setSize(new Dimension(icons[i].getIconWidth(),
                              icons[i].getIconHeight()));
    }
}