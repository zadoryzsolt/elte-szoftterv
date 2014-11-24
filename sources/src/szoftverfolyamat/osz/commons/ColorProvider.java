package szoftverfolyamat.osz.commons;

import java.awt.Color;
import java.util.Random;

/*
Szinek kezeleseben segit.
*/
public class ColorProvider {
    /*
    Vilagosabba ill. sotetebbe teszi a  color  parametert  a  percent  
    szazalekos aranyban.
    */
    public static Color lighten(Color color, double percent) {
        double r = percent/100;
        int nr = (int)(color.getRed()*r);
        if(nr>255) {
            nr = 255;
        }
        int ng = (int)(color.getGreen()*r);
        if(ng>255) {
            ng = 255;
        }
        int nb = (int)(color.getBlue()*r);
        if(nb>255) {
            nb = 255;
        }
        return new Color(nr, ng, nb);
    }

    /*
    Masolatot keszit az input parameter szinrol.
    */
    public static Color copyOf(Color c) {
        if(c==null) {
            return null;
        }
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    /*
    A  percent  szazalekkal mattabba teszi a  color  szint.
    */
    public static Color matten(Color color, double percent) {
        double pr = percent/100;
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        double avg = (r + g + b)/3;
        int dr = (int)((r - avg)*pr);
        int dg = (int)((g - avg)*pr);
        int db = (int)((b - avg)*pr);
        r = r - dr;
        g = g - dg;
        b = b - db;
        return new Color(r, g, b);
    }

    private static Random random = new Random();

    /*
    Veletlen szint ad vissza.
    */
    public static Color randomColor() {
        return new Color(random.nextInt() | 0xff000000);
    }

    /*
    String alapjan szin
    */
    public static Color colorByRgbString(String s) {
        String r = s.substring(0, 2);
        String g = s.substring(2, 4);
        String b = s.substring(4, 6);
        return new Color(Integer.parseInt(r, 16),
                         Integer.parseInt(g, 16),
                         Integer.parseInt(b, 16));
    }
    
    /*
    Int alapjan szin
    */
    public static Color colorByInt(int i) {
        return new Color(i);
    }    
}
