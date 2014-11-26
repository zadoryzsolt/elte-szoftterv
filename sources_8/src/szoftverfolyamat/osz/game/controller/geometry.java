package szoftverfolyamat.osz.game.controller;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Dimension;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import java.util.Hashtable;

import szoftverfolyamat.osz.game.controller.sprites.BoundingShape;
import szoftverfolyamat.osz.game.controller.sprites.AbstractSprite;
import szoftverfolyamat.osz.game.controller.sprites.BoundingShapeObserver;

class SpriteFarm implements BoundingShapeObserver {
    private final Hashtable<AbstractSprite, Point2D.Double> sprites;
    private final Vector<CollisionHandler> collisionHandlers;
    private final Vector<CollisionHandler> nocollisionHandlers;

    public SpriteFarm() {
        sprites = new Hashtable<AbstractSprite, Point2D.Double>();
        collisionHandlers = new Vector<CollisionHandler>();
        nocollisionHandlers = new Vector<CollisionHandler>();
    }

    @Override
    public synchronized void changed(BoundingShape shape) {
        checkCollisions((AbstractSprite)shape, CollisionHandler.SHAPE_CHANGED);
    }

    synchronized public Collection<AbstractSprite>
    moveBy(AbstractSprite s, Vector2D v) {
        final Point2D.Double p = sprites.get(s);
        if(p==null) {
            return new Vector<AbstractSprite>();
        }
        final double x = p.getX() + v.getX();
        final double y = p.getY() + v.getY();
        return moveTo(s, x, y);
    }

    synchronized public Collection<AbstractSprite>
    moveTo(AbstractSprite s,  double x, double y) {
        sprites.put(s, new Point2D.Double(x, y));
        return checkCollisions(s, CollisionHandler.MOVED);
    }

    synchronized void addCollisionHandler(CollisionHandler h) {
        collisionHandlers.add(h);
    }

    synchronized void addNocollisionHandler(CollisionHandler h) {
        nocollisionHandlers.add(h);
    }

    synchronized void remove(AbstractSprite s) {
        sprites.remove(s);
    }

    private Collection<AbstractSprite> checkCollisions(AbstractSprite sp, int ctype) {
        final Vector<AbstractSprite> vector = new Vector<AbstractSprite>();
        final Dimension spd = sp.getPrimaryBoundingBox();
        final double spw = spd.getWidth();
        final double sph = spd.getHeight();
        final Point2D.Double splp = sprites.get(sp);
        final double spx = splp.getX();
        final double spy = splp.getY();
        final Rectangle2D.Double sprec = new Rectangle2D.Double(spx, spy, spw, sph);
        for(AbstractSprite s : sprites.keySet()) {
            if(s!=sp) {
                final Dimension sd = s.getPrimaryBoundingBox();
                final double sw = sd.getWidth();
                final double sh = sd.getHeight();
                final Point2D.Double slp = sprites.get(s);
                final double sx = slp.getX();
                final double sy = slp.getY();
                if(sprec.intersects(sx, sy, sw, sh)) {
                    vector.add(s);
                    final Iterator<CollisionHandler> chi =
                        collisionHandlers.iterator();
                    for(CollisionHandler h : collisionHandlers) {
                        h.collision(s, sp, ctype);
                    }
                }
            }
        }
        if(vector.isEmpty()) {
            for(CollisionHandler h : nocollisionHandlers) {
                  h.collision(null, sp, -1);
            }
        }
        return vector;
    }
}

interface CollisionHandler {
    final int MOVED = 1;
    final int SHAPE_CHANGED = 2;

    void collision(AbstractSprite s, AbstractSprite changed, int t);
}

class Vector2D extends Point2D.Double {
    public Vector2D() {
        super();
    }

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public void setCoordinates(double x, double y) {
        setLocation(x, y);
    }

    public Vector2D getReverse() {
        return new Vector2D(-getX(), -getY());
    }

    public Vector2D getScaledCopy(double r) {
        return new Vector2D(r*getX(), r*getY());
    }
}
