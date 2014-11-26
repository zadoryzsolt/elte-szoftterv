package szoftverfolyamat.osz.game.view;

import javax.swing.ImageIcon;
import java.awt.Font;

/*
[view::graphics <--- game ctrl]   
*/
public interface GameCtrlCallsViewGraphics {
    void initStage(int w, int h, int color);

    Sprite registerSprite(ImageIcon[] imga);    
    
    Sprite registerSprite(double w, double h, Font f, String s);
    
    void moveCanvas(double x, double y);
}


