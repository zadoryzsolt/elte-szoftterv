package szoftverfolyamat.osz.commons.graphics;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public abstract class ModalDialog extends JDialog {
    public static JFrame jframe;

    protected final GenericPanel buttonRow;
    protected final GenericPanel bgPanel;
    protected final JTextArea textArea;

    public ModalDialog(String t, int w, int h) {
        super(ModalDialog.jframe, t);
        setModal(true);
        setFont(GraphicConstants.STANDARD_FONT);
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        textArea.setFont(GraphicConstants.STANDARD_FONT);
        buttonRow = new GenericPanel();
        buttonRow.setBoxLayout(true);
        buttonRow.setFont(GraphicConstants.STANDARD_FONT);
        buttonRow.setOpaque(false);
        buttonRow.setEmptyBorder(5);
        bgPanel = new GenericPanel();
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(buttonRow, BorderLayout.SOUTH);
        bgPanel.add(textArea, BorderLayout.CENTER);
        bgPanel.setEmptyBorder(5);
        add(bgPanel);
        setSize(w, h);
        setResizable(false);
        int fw = ModalDialog.jframe.getWidth();
        int fh = ModalDialog.jframe.getHeight();
        double fpx = ModalDialog.jframe.getLocation().getX();
        double fpy = ModalDialog.jframe.getLocation().getY();
        setLocation((int)(fpx + fw/2 - w/2), (int)(fpy + fh/2 - h/2));
    }

    public void addButton(GenericButton b) {
        buttonRow.add(b);
        b.setFont(GraphicConstants.STANDARD_FONT);
        buttonRow.add(new GenericLabel(0, "  "));
        buttonRow.revalidate();
    }

    public void setText(String s) {
        textArea.setText(s);
    }

    public String getText() {
        return textArea.getText();
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setColor(int c) {
        textArea.setBackground(new Color(c));
        bgPanel.setBackground(new Color(c));
    }

    public void setButtonColor(int c) {
        ;
    }

    protected void dropDialog() {
        setVisible(false);
        dispose();
    }

    public void setVisibleLater() {
        bgPanel.invokeLater(
            new Runnable() {
                public void run() {
                    setVisible(true);
                    revalidate();
                }
            }
        );
    }
}

