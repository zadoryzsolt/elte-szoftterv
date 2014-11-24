package szoftverfolyamat.osz.commons.graphics;

import java.awt.Color;

public abstract class MessageBox extends ModalDialog {
    public MessageBox(String t, int w, int h) {
        super(t, w, h);
        addButton(new GenericButton("OK",
                                    GraphicConstants.GRAY_BUTTON_COLOR_INT,
                                    GraphicConstants.GRAY_BUTTON_HILITE_COLOR_INT,
                                    GraphicConstants.LIGHT_TEXT_COLOR_INT) {
                      @Override
                      protected void onClick() {
                          clicked();
                      }
                  }
                 );
        setColor(GraphicConstants.GRAY_BG_COLOR_INT);
        textArea.setForeground(new Color(GraphicConstants.LIGHT_TEXT_COLOR_INT));
    }

    protected abstract void clicked();
}