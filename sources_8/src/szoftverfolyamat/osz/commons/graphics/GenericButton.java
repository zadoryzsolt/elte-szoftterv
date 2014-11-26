package szoftverfolyamat.osz.commons.graphics;

import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.Color;

public abstract class GenericButton extends JButton {
    private int bgColor;
    private int moverColor;

    public GenericButton(String s, int c, int cmover, int t) {
        super(s);
        setBackground(c);
        setForeground(t);
        bgColor = c;
        moverColor = cmover;
        setFocusPainted(false);

        addActionListener(new ActionListener() {
                              @Override
                              public void actionPerformed(ActionEvent e) {
                                  onClick();
                              }
                          }
                         );

        addMouseListener(new MouseAdapter() {
                             @Override
                             public void mouseEntered(MouseEvent e)  {
                                 if(isEnabled()) {
                                     setBackground(moverColor);
                                 }
                             }

                             @Override
                             public void mouseExited(MouseEvent e) {
                                 if(isEnabled()) {
                                     setBackground(bgColor);
                                 }
                             }
                         }
                        );
    }

    protected abstract void onClick();

    public void setBackground(int c) {
        setBackground(new Color(c));
        repaint();
    }

    public void setForeground(int c) {
        setForeground(new Color(c));
        repaint();
    }

    public void invokeLater(Runnable r) {
        SwingUtilities.invokeLater(r);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setBackground(new Color(bgColor));
        repaint();
    }
    
    public boolean isRequestFocusEnabled() {
        return false;
    }
}