package szoftverfolyamat.osz.game.controller;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import java.util.Hashtable;

import szoftverfolyamat.osz.commons.ImageProvider;
import szoftverfolyamat.osz.game.controller.sprites.AbstractSprite;
import szoftverfolyamat.osz.game.controller.sprites.BitmapSprite;
import szoftverfolyamat.osz.game.controller.sprites.Wall;
import szoftverfolyamat.osz.game.controller.sprites.Device;
import szoftverfolyamat.osz.game.controller.sprites.Moving;
import szoftverfolyamat.osz.game.controller.sprites.Animated;
import szoftverfolyamat.osz.game.controller.sprites.Foe;
import szoftverfolyamat.osz.game.controller.sprites.AllowsCollision;
import szoftverfolyamat.osz.game.controller.sprites.BoundingShapeObserver;
import szoftverfolyamat.osz.game.view.Sprite;

abstract class AbstractSpriteImpl implements AbstractSprite {
    private Dimension primaryBoundingBox;
    private BoundingShapeObserver boundingShapeObserver;

    protected void setPrimaryBoundingBox(Dimension p) {
        primaryBoundingBox = p;
    }

    int getHeight() {
        return (int)primaryBoundingBox.getHeight();
    }

    int getWidth() {
        return (int)primaryBoundingBox.getWidth();
    }

    @Override
    public Dimension getPrimaryBoundingBox() {
        return primaryBoundingBox;
    }

    @Override
    public Rectangle2D[] getRefinementBoundingBoxes() {
        return null;
    }

    @Override
    public void setBoundingShapeObserver(BoundingShapeObserver o) {
        boundingShapeObserver = o;
    }
}

abstract class BitmapSpriteImpl extends AbstractSpriteImpl
    implements BitmapSprite {
    
    private ImageIcon[] icons;

    static ImageIcon[] loadIcons(Class klass, String[] filenames, int w, int h) {
        int len = filenames.length;
        ImageIcon[] ic = new ImageIcon[len];
        for(int i=0; i<len; i++) {
            ic[i] = ImageProvider.loadIcon(klass, filenames[i], w, h);
        }
        return ic;
    }

    static ImageIcon[] loadSingleIcon(Class klass, String filename, int w, int h) {
        ImageIcon[] ic = new ImageIcon[1];
        ic[0] = ImageProvider.loadIcon(klass, filename, w, h);
        return ic;
    }

    protected void setIcons(ImageIcon[] p) {
        icons = p;
    }

    protected ImageIcon getIcon() {
        return icons[0];
    }

    @Override
    public ImageIcon[] getIcons() {
        return icons;
    }
}

final class DefaultWallFactory {
    private static Hashtable<Dimension, ImageIcon> redBricksTable;
    private static Hashtable<Dimension, ImageIcon> darkBricksTable;
    private static Hashtable<Dimension, ImageIcon> yellowBricksTable;
    private static Hashtable<Dimension, ImageIcon> stoneBricksTable;

    static  {
        redBricksTable = new Hashtable<Dimension, ImageIcon>();
        yellowBricksTable = new Hashtable<Dimension, ImageIcon>();
        darkBricksTable = new Hashtable<Dimension, ImageIcon>();
        stoneBricksTable = new Hashtable<Dimension, ImageIcon>();
    }

    private static BitmapSpriteImpl createBrickWall(String path,
            Hashtable<Dimension, ImageIcon> ht,
            int w, int h) {
        Dimension d = new Dimension(w, h);
        ImageIcon icon = ht.get(d);
        if(icon==null) {
            icon = ImageProvider.loadIcon(DefaultWallFactory.class,
                                          path, w, h);
            ht.put(d, icon);
        }
        return new DefaultWallFactory.BrickWall(icon, w, h);
    }

    static BitmapSpriteImpl createRedBrickWall(int w, int h) {
        return createBrickWall("img/bricks/red_bricks-1_1.png",
                               redBricksTable, w, h);
    }

    static BitmapSpriteImpl createYellowBrickWall(int w, int h) {
        return createBrickWall("img/bricks/yellow_bricks-1_1.png",
                               yellowBricksTable, w, h);
    }

    static BitmapSpriteImpl createDarkBrickWall(int w, int h) {
        return createBrickWall("img/bricks/dark_bricks-1_45.png",
                               darkBricksTable, w, h);
    }

    static BitmapSpriteImpl createStoneBrickWall(int w, int h) {
        return createBrickWall("img/bricks/stone_bricks-1_1.png",
                               darkBricksTable, w, h);
    }

    static class BrickWall extends BitmapSpriteImpl implements BitmapSprite, Wall {
        public BrickWall(ImageIcon ic, int w, int h) {
            ImageIcon[] i = new ImageIcon[1];
            i[0] = ic;
            setIcons(i);
            setPrimaryBoundingBox(new Dimension(w, h));
        }
    }
}

final class Computer extends BitmapSpriteImpl implements Device, AllowsCollision  {
    public Computer(int w, int h) {
        setIcons(BitmapSpriteImpl.loadSingleIcon(Player.class,
                 "img/computer-1_00.png", w, h));
        setPrimaryBoundingBox(new Dimension(w, h));
    }

    @Override
    public void activate() {}
}

final class SavePoint extends BitmapSpriteImpl implements Device  {
    public SavePoint(int w, int h) {
        setIcons(BitmapSpriteImpl.loadSingleIcon(SavePoint.class,
                 "img/save_point-1_03.png", w, h));
        setPrimaryBoundingBox(new Dimension(w, h));
    }

    @Override
    public void activate() {}
}

final class Exit extends BitmapSpriteImpl implements AllowsCollision {
    public Exit(int w, int h) {
        setIcons(BitmapSpriteImpl.loadSingleIcon(Exit.class,
                 "img/exit-2_00.png", w, h));
        setPrimaryBoundingBox(new Dimension(w, h));
    }
}

class Ghost extends BitmapSpriteImpl implements Foe, Moving, Animated {
    
    private final int[] frames = {0, 1, 2, 1}; 
    private int seq;
    
    public Ghost(int w, int h) {
                String[] files = {"img/ghosts/ghost-1a-0_95.png",
                               "img/ghosts/ghost-1b-0_95.png",
                               "img/ghosts/ghost-1c-0_95.png"};
        setIcons(BitmapSpriteImpl.loadIcons(Ghost.class, files, w, h));
        setPrimaryBoundingBox(new Dimension(w, h));
        seq = 0;
    }

    @Override
    public int scoreDecrease() {
        return 1;
    }

    @Override
    public boolean instantlyKills() {
        return true;
    }

    @Override
    public Dimension getTranslationVector() {
        return null;
    }
    
    @Override
    public int getNextFrame() {
        seq++;
        if(seq==frames.length) {
            seq = 0;
        }
        return frames[seq];
    }
}

final class Trap extends BitmapSpriteImpl implements Foe {
    public Trap(int w, int h) {
        setIcons(BitmapSpriteImpl.loadSingleIcon(Trap.class,
                 "img/trap-1_1.png", w, h));
        setPrimaryBoundingBox(new Dimension(w, h));
    }
 
    @Override
    public int scoreDecrease() {
        return 1;
    }

    @Override
    public boolean instantlyKills() {
        return true;
    }    
}

final class Player extends BitmapSpriteImpl implements Moving {
    public Player(int w, int h) {
        setIcons(BitmapSpriteImpl.loadSingleIcon(Player.class,
                 "img/player-0_71.png", w, h));
        setPrimaryBoundingBox(new Dimension(w, h));
    }

    @Override
    public Dimension getTranslationVector() {
        return null;
    }
}


