package szoftverfolyamat.osz.commons;

import java.awt.Image;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageProvider {

    public static Image loadImage(Class klass, String path,
                                  int width, int height) {
        Image img = loadImage(klass, path);
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static Image loadImage(Class klass, String path) {
        Image ret = null;
        try {
            InputStream is =
                klass.getClassLoader().getSystemResourceAsStream(path);
            ret = ImageIO.read(is);
        } catch(Exception e) {
            ;
        }
        return ret;
    }

    public static ImageIcon loadIcon(Class klass, String path,
                                     int width, int height) {
        return new ImageIcon(loadImage(klass, path, width, height));
    }

    public static ImageIcon loadIcon(Class klass, String path) {
        return new ImageIcon(loadImage(klass, path));
    }
}
