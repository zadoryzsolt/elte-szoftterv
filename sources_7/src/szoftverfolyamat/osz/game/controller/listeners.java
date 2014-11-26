package szoftverfolyamat.osz.game.controller;

import szoftverfolyamat.osz.game.controller.sprites.AbstractSprite;
import szoftverfolyamat.osz.game.controller.sprites.Wall;
import szoftverfolyamat.osz.game.view.Sprite;
import szoftverfolyamat.osz.game.view.GameCtrlCallsViewInfoPanel;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;

class TickListenerSet implements TickListener {
    private HashSet<TlEntry> entries;

    public TickListenerSet() {
        entries = new HashSet<TlEntry>();
    }

    synchronized void add(TickListener tl, int pc) {
        entries.add(new TlEntry(tl, pc));
    }

    @Override
    synchronized public void tick() {
        for(TlEntry e: entries) {
            e.tick();
        }
    }

    class TlEntry implements TickListener {
        private final TickListener tickListener;
        private final int perCount;
        private int counter;

        public TlEntry(TickListener l, int pc) {
            counter = 0;
            perCount = pc;
            tickListener = l;
        }

        @Override
        public void tick() {
            counter++;
            if(counter==perCount) {
                counter = 0;
                tickListener.tick();
            }
        }
    }
}

interface TickListener {
    void tick();
}

class PlayerTickListener implements TickListener {
    private final double delta = 4;
    private final SpriteFarm spriteFarm;
    private final Sprite playerSprite;
    private final AbstractSprite player;
    private ViewControlsCallsGameCtrl.Direction direction;

    public PlayerTickListener(SpriteFarm sf, Sprite ps, AbstractSprite p) {
        direction = ViewControlsCallsGameCtrl.Direction.NIL;
        playerSprite = ps;
        spriteFarm = sf;
        player = p;
    }

    void setDirection(ViewControlsCallsGameCtrl.Direction d) {
        direction = d;
    }

    @Override
    public void tick() {
        Vector2D vector = new Vector2D();
        if(direction==ViewControlsCallsGameCtrl.Direction.W) {
            vector.setLocation(-delta, 0);
        } else if(direction==ViewControlsCallsGameCtrl.Direction.E) {
            vector.setLocation(delta, 0);
        } else if(direction==ViewControlsCallsGameCtrl.Direction.S) {
            vector.setLocation(0, delta);
        } else if(direction==ViewControlsCallsGameCtrl.Direction.N) {
            vector.setLocation(0, -delta);
        } else if(direction==ViewControlsCallsGameCtrl.Direction.NE) {
            vector.setLocation(delta, -delta);
        } else if(direction==ViewControlsCallsGameCtrl.Direction.SE) {
            vector.setLocation(delta, delta);
        } else if(direction==ViewControlsCallsGameCtrl.Direction.NW) {
            vector.setLocation(-delta, -delta);
        } else if(direction==ViewControlsCallsGameCtrl.Direction.SW) {
            vector.setLocation(-delta, delta);
        }

        if(direction!=ViewControlsCallsGameCtrl.Direction.NIL) {
            Collection<AbstractSprite> colls =
                spriteFarm.moveBy(player, vector);
            if(!containsWall(colls)) {
                playerSprite.moveBy(vector.getX(), vector.getY());
            } else {
                spriteFarm.moveBy(player, vector.getReverse());
                vector = vector.getScaledCopy(0.5);
                colls = spriteFarm.moveBy(player, vector);
                if(!containsWall(colls)) {
                    playerSprite.moveBy(vector.getX(), vector.getY());
                } else {
                    spriteFarm.moveBy(player, vector.getReverse());
                    vector = vector.getScaledCopy(0.5);
                }
            }
        }
    }

    private boolean containsWall(Collection<AbstractSprite> c) {
        boolean found = false;
        Iterator<AbstractSprite> it = c.iterator();
        while((!found)&&it.hasNext()) {
            if(it.next() instanceof Wall) {
                found = true;
            }
        }
        return found;
    }
}

//------------------------------------------------------------------------------

class ScoreTickListener implements TickListener {
    private final Ticker ticker;
    private final InternalEvents ieif;
    private final GameCtrlCallsViewInfoPanel vipIf;
    private int score;

    public ScoreTickListener(Ticker t, GameCtrlCallsViewInfoPanel i,
                             InternalEvents ie) {
        ticker = t;
        vipIf = i;
        setScore(100000);
        ieif = ie;
    }

    @Override
    public synchronized void tick() {
        score--;
        checkScore();
        setScore(score);
        int hi = (int)(ticker.getSecondsLeft() / 3600);
        int mi = (int)((ticker.getSecondsLeft() - 3600*hi) / 60);
        int si = (int)(ticker.getSecondsLeft() % 60);
        String hs = Integer.toString(hi);
        if(hs.length()==1) {
            hs = "0" + hs;
        }
        String ms = Integer.toString(mi);
        if(ms.length()==1) {
            ms = "0" + ms;
        }
        String ss = Integer.toString(si);
        if(ss.length()==1) {
            ss = "0" + ss;
        }
        vipIf.setTime(hs, ms, ss);
    }

    public void decreaseScore(int t) {
        score = score - t;
        tick();
    }

    private void checkScore() {
        if(score==0)  {
            ieif.playerDied();
        }
    }

    private void setScore(int sc) {
        if(sc<0) {
            sc = 0;
        }
        score = sc;
        String s = Integer.toString(score);
        while(s.length()<6) {
            s = "0" + s;
        }
        vipIf.setScore(s);
    }
}

//------------------------------------------------------------------------------

/*
class MainCollisionHandler implements CollisionHandler {
    private final InternalEvents internalEvents;

    public MainCollisionHandler(InternalEvents i) {
        internalEvents = i;
    }

    @Override
    public boolean collision(AbstractSprite s,
                             AbstractSprite changed, int t) {
        boolean ret = false;
        if(state==MainState.PLAYING) {
            if(changed instanceof Player) {
                if(s instanceof Exit) {
                    if(!levels.iterator().hasNext()) {
                        gameOver(true);
                    } else {
                        levelCompleted();
                    }
                } else if(s instanceof SavePoint) {
                    playingState = PlayingState.AT_SAVEPOINT;
                    ret = true;
                } else if(s instanceof Computer) {
                    playingState = PlayingState.AT_COMPUTER;
                    ret = true;
                } else if(s instanceof Foe) {
                    playerDied();
                    ret = true;
                }
            } else if (s instanceof Player) {
                if(changed instanceof Foe) {
                    playerDied();
                }
                ret = true;
            }
        }
        return ret;
    }
}
*/