package szoftverfolyamat.osz.game.view;

import szoftverfolyamat.osz.commons.graphics.GenericLabel;
import szoftverfolyamat.osz.commons.graphics.GenericPanel;
import szoftverfolyamat.osz.commons.graphics.GraphicConstants;
import szoftverfolyamat.osz.commons.FontProvider;
import szoftverfolyamat.osz.commons.ColorProvider;
import szoftverfolyamat.osz.commons.ImageProvider;
import szoftverfolyamat.osz.game.view.GameCtrlCallsViewGraphics;

import java.awt.Font;
import java.awt.Point;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class GameArea extends GenericPanel implements GameCtrlCallsViewGraphics {
    private boolean active;
    private ScrollablePanel panel;
    private JScrollPane scrollpane;
    private final GameAreaKeyAdapter gaka;

    public GameArea(GameAreaKeyAdapter a) {
        super();
        setFocusable(true);
        setOpaque(false);
        gaka = a;
        addKeyListener(a);
        setBorderLayout();
        clear(10, 10);
        active = false;
        addFocusListener(
            new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    ;
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(e.getID()==FocusEvent.FOCUS_LOST) {
                        gaka.stopMoving();
                    }
                }
            }
        );
    }

    private void clear(int w, int h) {
        removeAll();
        panel = new ScrollablePanel(w, h);
        panel.setNullLayout();
        scrollpane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.getViewport().setBackground(new Color(GraphicConstants.GRAY_1_INT));
        addToCenter(scrollpane);
        revalidate();
    }

    void setActive(boolean b) {
        active = b;
        requestFocusInWindow(active);
        gaka.focusRestored();
    }

    boolean isActive() {
        return active;
    }

    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
    }

    @Override
    public void initStage(int w, int h, int color) {
        clear(w, h);
        panel.setBounds(0, 0, w, h);
        panel.setBackground(color);
        panel.repaint();
    }

    @Override
    public Sprite registerSprite(ImageIcon[] imga) {
        SpriteImpl ret = new SpriteImpl(imga);
        panel.add(ret);
        panel.repaint();
        return ret;
    }

    @Override
    public void moveCanvas(double x, double y) {
        //Point p = scrollpane.getViewport().getViewPosition();
        //Point r = new Point((int)(p.getX()+x), (int)(p.getY()+y));
        //scrollpane.getViewport().setViewPosition(r); scrollpane.repaint();
    }
    
    @Override
    public Sprite registerSprite(double w, double h, Font f, String s) {
        return null;
    }
}

class ScrollablePanel extends GenericPanel implements Scrollable {
    private int pw;
    private int ph;

    public ScrollablePanel(int w, int h) {
        ph = h;
        pw = w;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(pw, ph);
    }


    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(pw, ph);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation,
                                          int direction) {
        return 10;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        return 10;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
