package szoftverfolyamat.osz.commons;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.io.InputStream;

/*
Adott jar file-ban egy eleresi uton talalhato fontokat eri el ez az osztaly.
*/
public class FontProvider {
    private static Font regular;
    private static Font regularItalic;
    private static Font bold;
    private static Font boldItalic;
    private static Font monospace;
    private static Font monospaceBold;
    private static AffineTransform transform;

    /*
    Meg kell hivni ezt eloszor. Betolti a fontokat. pld.:
    FontProvider.init("fonts", "GOTHIC.TTF", "GOTHICI.TTF",
                              "GOTHICB.TTF", "GOTHICBI.TTF", "lucon.ttf", 1, 1);
    */
    public static boolean init(String resourcePath,
                               String r, String ri, String b, String bi,
                               String ms, String msb,
                               double tx, double ty) {
        boolean ret = true;
        transform = new AffineTransform();
        transform.scale(tx, ty);
        try {
            InputStream is =
                ClassLoader.getSystemResourceAsStream(resourcePath + "/" + r);
            regular = Font.createFont(Font.TRUETYPE_FONT, is);
            is = ClassLoader.getSystemResourceAsStream(resourcePath + "/" + ri);
            regularItalic = Font.createFont(Font.TRUETYPE_FONT, is);
            is = ClassLoader.getSystemResourceAsStream(resourcePath + "/" + b);
            bold = Font.createFont(Font.TRUETYPE_FONT, is);
            is = ClassLoader.getSystemResourceAsStream(resourcePath + "/" + bi);
            boldItalic = Font.createFont(Font.TRUETYPE_FONT, is);
            is = ClassLoader.getSystemResourceAsStream(resourcePath + "/" + ms);
            monospace = Font.createFont(Font.TRUETYPE_FONT, is);
            is = ClassLoader.getSystemResourceAsStream(resourcePath + "/" + msb);
            monospaceBold = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch(Exception e) {
            ret = false;
            System.err.println(e + "\n" + e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }
    /*
    Bold Terminal font lekerese.
    */
    public static Font getMonospaceBold(float sizePx) {
        return monospaceBold.deriveFont(sizePx).deriveFont(transform);
    }

    /*
    Terminal font lekerese.
    */
    public static Font getMonospace(float sizePx) {
        return monospace.deriveFont(sizePx).deriveFont(transform);
    }

    /*
    Normal font lekerese.
    */
    public static Font getRegular(float sizePx) {
        return regular.deriveFont(sizePx).deriveFont(transform);
    }

    /*
    Normal dolt font lekerese.
    */
    public static Font getRegularItalic(float sizePx) {
        return regularItalic.deriveFont(sizePx).deriveFont(transform);
    }

    /*
    Felkover font lekerese.
    */
    public static Font getBold(float sizePx) {
        return bold.deriveFont(sizePx).deriveFont(transform);
    }

    /*
    Felkover dolt font lekerese.
    */
    public static Font getBoldItalic(float sizePx) {
        return boldItalic.deriveFont(sizePx).deriveFont(transform);
    }

    /*
    Font valahonnan
    */
    public static Font getFont(Class klass, String path, float height) {
        Font ret = null;
        try {
            InputStream is =
                klass.getClassLoader().getSystemResourceAsStream(path);
            ret = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(height);
        } catch(Exception e) {
            ;
        }
        return ret;
    }
}
