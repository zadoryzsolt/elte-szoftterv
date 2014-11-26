package szoftverfolyamat.osz.commons.graphics;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;

import szoftverfolyamat.osz.commons.FontProvider;
import szoftverfolyamat.osz.commons.ColorProvider;

public class GraphicConstants {
    static {
        boolean b = FontProvider.init("fonts", "GOTHIC.TTF", "GOTHICI.TTF",
                                      "GOTHICB.TTF", "GOTHICBI.TTF",
                                      "UbuntuMono-R.ttf",
                                      "UbuntuMono-B.ttf", 1, 1);
        if(!b) {
            System.err.println("Startup error - fonts");
            System.exit(-1);
        }
    }
    
    public final static float STD_FONT_HEIGHT = 14f;

    public final static Font STANDARD_FONT =
        FontProvider.getRegular(STD_FONT_HEIGHT);
    public final static Font STANDARD_BOLD_FONT =
        FontProvider.getBold(STD_FONT_HEIGHT);
    public final static Font BIG_STANDARD_BOLD_FONT =
        FontProvider.getBold(STD_FONT_HEIGHT*1.3f);
    public final static Font MONOSPACE_FONT =
        FontProvider.getMonospace(STD_FONT_HEIGHT);
    public final static Font MONOSPACE_BOLD_FONT =
        FontProvider.getMonospaceBold(STD_FONT_HEIGHT);
    public final static Font BIG_MONOSPACE_BOLD_FONT =
        FontProvider.getMonospaceBold(STD_FONT_HEIGHT*1.5f);

    public final static int STANDARD_BG_COLOR_INT = 0x667766;
    public final static Color STANDARD_BG_COLOR = new Color(STANDARD_BG_COLOR_INT);
    public final static int BUTTON_HILITE_COLOR_INT = 0x889988;
    public final static int BUTTON_COLOR_INT = 0x778877;
    public final static int GRAY_BG_COLOR_INT = 0x777777;
    public final static int GRAY_BUTTON_HILITE_COLOR_INT = 0x999999;
    public final static int GRAY_BUTTON_COLOR_INT = 0x888888;
    public final static int GRAY_1_INT = 0x555555;
    public final static int GRAY_0_INT = 0x333333;
    public final static int GRAY_2_INT = 0x777777;
    public final static int LIGHT_TEXT_COLOR_INT = 0xeeeeee;
    public final static Color LIGHT_TEXT_COLOR = new Color(LIGHT_TEXT_COLOR_INT);

    public static void invokeLater(Runnable r) {
        SwingUtilities.invokeLater(r);
    }
}