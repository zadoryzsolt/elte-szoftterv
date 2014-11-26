package szoftverfolyamat.osz.commons.graphics;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public abstract class AreYouSureDialog extends ModalDialog {
    public AreYouSureDialog(String t, int w, int h) {
        super(t, w, h);
        addButton(new GenericButton("Igen",
                                    GraphicConstants.GRAY_BUTTON_COLOR_INT,
                                    GraphicConstants.GRAY_BUTTON_HILITE_COLOR_INT,
                                    GraphicConstants.LIGHT_TEXT_COLOR_INT) {
                      @Override
                      protected void onClick() {
                          yesClicked();
                      }
                  }
                 );
        addButton(new GenericButton("Nem",
                                    GraphicConstants.GRAY_BUTTON_COLOR_INT,
                                    GraphicConstants.GRAY_BUTTON_HILITE_COLOR_INT,
                                    GraphicConstants.LIGHT_TEXT_COLOR_INT) {
                      @Override
                      protected void onClick() {
                          noClicked();
                      }
                  }
                 );
        setColor(GraphicConstants.GRAY_BG_COLOR_INT);
        textArea.setForeground(new Color(GraphicConstants.LIGHT_TEXT_COLOR_INT));
    }

    protected abstract void yesClicked();

    protected void noClicked() {
        dropDialog();
    }
}
