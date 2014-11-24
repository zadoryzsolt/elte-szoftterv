package szoftverfolyamat.osz.game.controller;

import szoftverfolyamat.osz.game.view.GameCtrlCallsViewInfoPanel;
import szoftverfolyamat.osz.game.view.GameCtrlCallsViewGraphics;
import szoftverfolyamat.osz.game.view.GameCtrlCallsViewLifecycle;
import szoftverfolyamat.osz.model.Mission;
import szoftverfolyamat.osz.model.Game;
import szoftverfolyamat.osz.model.Level;
import szoftverfolyamat.osz.model.SourceFragment;
import szoftverfolyamat.osz.menu.GameCtrlCallsMenu;
import szoftverfolyamat.osz.game.view.Sprite;
import szoftverfolyamat.osz.commons.ImageProvider;
import szoftverfolyamat.osz.game.controller.sprites.AbstractSprite;
import szoftverfolyamat.osz.game.controller.sprites.Foe;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.util.Iterator;

public class EventProcessor implements
            MenuCallsGameCtrl, ViewTerminalCallsGameCtrl,
    ViewControlsCallsGameCtrl, JsCallsController, InternalEvents {

    private GameCtrlCallsViewInfoPanel vipIf;
    private GameCtrlCallsViewGraphics vgIf;
    private GameCtrlCallsViewLifecycle vlcIf;
    private final GameCtrlCallsMenu mif;
    private final int TICKS_PER_SECOND = 35;
    
    private Ticker ticker;
    private volatile MainState state;
    private volatile PlayingState playingState;
    private Iterator<Level> levels;
    private Level currentLevel;
    private TickListenerSet tickListenerSet;
    private SpriteFarm spriteFarm;
    private PlayerTickListener playerTickListener;
    private ScoreTickListener scoreTickListener;

    private enum MainState {
        PRE_START,
        PLAYING,
        AFTERGAME,
        BUILDING_LEVEL,
        INTERMEZZO,     
        HALTED
    }

    private enum PlayingState {
        STROLLING,
        AT_SAVEPOINT,
        AT_COMPUTER,
        PROGRAMMING,
        PAUSED
    }

    public EventProcessor(GameCtrlCallsMenu m) {
        mif = m;
        state = MainState.PRE_START;
    }

    public void setViewInterfaces(GameCtrlCallsViewInfoPanel v1,
                                  GameCtrlCallsViewGraphics v2,
                                  GameCtrlCallsViewLifecycle v3) {
        vipIf = v1;
        vgIf = v2;
        vlcIf = v3;
    }

    private synchronized void clockTicked() {
        if(state==MainState.PLAYING) {
            tickListenerSet.tick();
        }
    }

    private synchronized void clockTimeout() {
        if(state==MainState.PLAYING) {
            state = MainState.AFTERGAME;
            vlcIf.defeat("");
        }
    }

    /*
    MenuCallsGameCtrl impl
    */
    @Override
    public synchronized void exitProgram() {
        state = MainState.HALTED;
        ticker.dispose();
        vlcIf.exit();
    }

    private PlayingState prePauseState;

    @Override
    public synchronized void pause() {
        if((state==MainState.PLAYING)&&(playingState!=PlayingState.PAUSED)) {
            prePauseState = playingState;
            playingState = PlayingState.PAUSED;
            playerTickListener.setDirection(Direction.NIL);
            vlcIf.pause();
            ticker.pause();
        } else {
            resume();
        }
    }

    @Override
    public synchronized void resume() {
        if((state==MainState.PLAYING)&&(playingState==PlayingState.PAUSED)) {
            ticker.kontinue();
            playingState = prePauseState;
            vlcIf.resume();
        }
    }

    @Override
    public synchronized void start(Mission m, String pn, GameCtrlCallsMenu cbi) {
        if(state==MainState.PRE_START) {
            levels = m.getLevels().iterator();
            currentLevel = levels.next();
            startGame(pn);
        }
    }

    @Override
    public synchronized void start(Game m, String pn, GameCtrlCallsMenu cbi) {
        if(state==MainState.PRE_START) {
            exitProgram();
            mif.error("- game serialization not implemented -");
        }
    }

    private void startGame(String pn) {
        vipIf.setPlayerInfo(pn);
        vlcIf.exitTerminal();
        ticker = new Ticker() {
                     protected void tick() {
                         clockTicked();
                     }

                     protected void timeout() {
                         clockTimeout();
                     }
                 };
        scoreTickListener = new ScoreTickListener(ticker, vipIf, this);

        String s = initLevel(getCurrentLevelCode());
        if(s!=null) {
            vlcIf.gameAborted();
            System.out.println(s);
        }
        vlcIf.setSource(currentLevel.getFragments());
        ticker.start(3601, (int)(1000/TICKS_PER_SECOND));
    }

    private String getCurrentLevelCode() {
        String levelCode = "";
        for(SourceFragment f : currentLevel.getFragments()) {
            levelCode = levelCode + f.getCode() + "\n";
        }
        return levelCode;
    }

    /*
    internal events impl
    */
    public synchronized String initLevel(String source) {
        vipIf.setLevelInfo(currentLevel.getName());
        spriteFarm = new SpriteFarm();
        spriteFarm.addCollisionHandler(
            new CollisionHandler() {
                @Override
                public void collision(AbstractSprite s,
                                      AbstractSprite changed, int t) {
                    if(state==MainState.PLAYING) {
                        if(changed instanceof Player) {
                            if(s instanceof Exit) {
                                if(!levels.hasNext()) {
                                    gameOver(true);
                                } else {
                                    levelCompleted();
                                }
                            } else if(s instanceof SavePoint) {
                                playingState = PlayingState.AT_SAVEPOINT;
                            } else if(s instanceof Computer) {
                                playingState = PlayingState.AT_COMPUTER;
                            } else if(s instanceof Foe) {
                                playerDied();
                            }
                        } else if (s instanceof Player) {
                            if(changed instanceof Foe) {
                                playerDied();
                            }
                        }
                    }
                }
            }
        );
        spriteFarm.addNocollisionHandler(
            new CollisionHandler() {
                public void collision(AbstractSprite s,
                                      AbstractSprite changed, int t) {
                    if(changed instanceof Player) {
                        if((playingState!=PlayingState.PROGRAMMING)&&
                                (state==MainState.PLAYING)) {
                            playingState = PlayingState.STROLLING;
                        }
                    }
                }
            }
        );
        tickListenerSet = new TickListenerSet();
        tickListenerSet.add(scoreTickListener, TICKS_PER_SECOND);

        state = MainState.BUILDING_LEVEL;
        final JavascriptHost jshost = new JavascriptHost();
        jshost.addReference("level", this);
        String s = jshost.runScript(source);
        if(s==null) {
            state = MainState.PLAYING;
            playingState = PlayingState.STROLLING;
        }
        return s;
    }

    @Override
    public synchronized void playerDied() {
        state = MainState.INTERMEZZO;
        vlcIf.playerDied("Maga meghalt.");
        vipIf.ping();
        SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                    ticker.pause();
                    initLevel(getCurrentLevelCode());
                    scoreTickListener.decreaseScore(1000);
                    ticker.kontinue();
                    ticker.decreaseTime(60);
                }
            }
        );
    }

    @Override
    public synchronized void levelCompleted() {
        state = MainState.INTERMEZZO;
        vlcIf.levelCompleted("");
    }

    @Override
    public synchronized void gameOver(boolean victory) {
        state = MainState.AFTERGAME;
        vlcIf.victory("");
    }

    /*
    ViewTerminalCallsGameCtrl impl
    */
    @Override
    public synchronized void executeCode(String code) {
        if((state==MainState.PLAYING)&&(playingState==PlayingState.PROGRAMMING)) {
            String s = initLevel(code);
            if(s!=null) {
                vipIf.runtimeError(s);
                initLevel(getCurrentLevelCode());
            }
            vlcIf.exitTerminal();
        }
    }

    @Override
    public synchronized void exitTerminal() {
        if((state==MainState.PLAYING)&&(playingState==PlayingState.PROGRAMMING)) {
            playingState = PlayingState.AT_COMPUTER;
            vlcIf.exitTerminal();
        }
    }

    /*
    ViewControlsCallsGameCtrl impl
    */
    @Override
    public synchronized void restartLevel() {
        if(state==MainState.PLAYING) {
            vlcIf.setSource(currentLevel.getFragments());
            if(playingState==PlayingState.PAUSED) {
                vlcIf.resume();
                resume();
            }
            if(playingState==PlayingState.PROGRAMMING) {
                vlcIf.exitTerminal();
            }
            initLevel(getCurrentLevelCode());
            playingState = PlayingState.STROLLING;
        }
    }

    @Override
    public synchronized void abortGame() {
        if(state==MainState.PLAYING) {
            state = MainState.AFTERGAME;
            vlcIf.gameAborted();
        }
    }

    @Override
    public synchronized void movePlayer(Direction d) {
        if((state==MainState.PLAYING)&&(playingState!=PlayingState.PAUSED)&&
                (playingState!=PlayingState.PROGRAMMING)) {
            playerTickListener.setDirection(d);
        }
    }

    @Override
    public synchronized void activate() {
        if(state==MainState.PLAYING) {
            if(playingState==PlayingState.AT_COMPUTER) {
                playingState = PlayingState.PROGRAMMING;
                playerTickListener.setDirection(Direction.NIL);
                vlcIf.enterTerminal();
            } else if(playingState==PlayingState.AT_SAVEPOINT) {
                System.out.println("save game not yet implemented."); // save game
                if(false) { // save error
                    vlcIf.exit();
                    mif.error("save error");
                    state = MainState.HALTED;
                }
            }
        }
    }

    @Override
    public synchronized void levelCompletionConfirmed() {
        if(state==MainState.INTERMEZZO) {
            currentLevel = levels.next();
            vlcIf.setSource(currentLevel.getFragments());
            String s = initLevel(getCurrentLevelCode());
            if(s!=null) {
                vlcIf.gameAborted();
                System.out.println(s);
            }
            state = MainState.PLAYING;
            playingState = PlayingState.STROLLING;
        }
    }

    @Override
    public synchronized void gameoverConfirmed(int furtherAction) {
        if(state==MainState.AFTERGAME) {
            if(furtherAction==ViewControlsCallsGameCtrl.BACK_TO_MAIN_MENU) {
                mif.backToMainMenu();
            } else if(furtherAction==ViewControlsCallsGameCtrl.LOAD_GAME) {
                mif.loadGame();
            } else if(furtherAction==ViewControlsCallsGameCtrl.SELECT_MISSION) {
                mif.startMission();
            } else {
                return;
            }
            state = MainState.HALTED;
            vlcIf.exit();
        }
    }

    /*
    Js - contoller 
    */
    @Override
    public synchronized void initialize(int w, int h, int bgColor) {
        if(state==MainState.BUILDING_LEVEL) {
            vgIf.initStage(w, h, bgColor);
        }
    }

    @Override
    public synchronized void setPlayer(int x, int y, int w, int h) {
        if(state==MainState.BUILDING_LEVEL) {
            final Player player = new Player(w, h);
            final ImageIcon[] icons = player.getIcons();
            final Sprite ps = vgIf.registerSprite(icons);
            ps.moveTo(x, y);
            spriteFarm.moveTo(player, x, y);
            playerTickListener = new PlayerTickListener(spriteFarm, ps, player);
            tickListenerSet.add(playerTickListener, 1);
        }
    }

    @Override
    public synchronized void createWall(String klass, int x, int y, int w, int h) {
        if(state==MainState.BUILDING_LEVEL) {
            BitmapSpriteImpl wall = null;
            if(klass.equals("RED_BRICK")) {
                wall = DefaultWallFactory.createRedBrickWall(w, h);
            } else if(klass.equals("YELLOW_BRICK")) {
                wall = DefaultWallFactory.createYellowBrickWall(w, h);
            } else if(klass.equals("DARK_BRICK")) {
                wall = DefaultWallFactory.createDarkBrickWall(w, h);
            } else if(klass.equals("STONE_BRICK")) {
                wall = DefaultWallFactory.createStoneBrickWall(w, h);
            }
            Sprite s = vgIf.registerSprite(wall.getIcons());
            s.moveTo(x, y);
            spriteFarm.moveTo(wall, x, y);
        }
    }

    @Override
    public synchronized void createComputer(int x, int y, int w, int h) {
        if(state==MainState.BUILDING_LEVEL) {
            final Computer computer = new Computer(w, h);
            final ImageIcon[] icons = computer.getIcons();
            Sprite cs = vgIf.registerSprite(icons);
            cs.moveTo(x, y);
            spriteFarm.moveTo(computer, x, y);
        }
    }

    @Override
    public synchronized void createSavePoint(int x, int y, int w, int h) {
        if(state==MainState.BUILDING_LEVEL) {
            final SavePoint savepoint = new SavePoint(w, h);
            final ImageIcon[] icons = savepoint.getIcons();
            Sprite cs = vgIf.registerSprite(icons);
            cs.moveTo(x, y);
            spriteFarm.moveTo(savepoint, x, y);
        }
    }

    @Override
    public synchronized void createExit(int x, int y, int w, int h) {
        if(state==MainState.BUILDING_LEVEL) {
            final Exit exit = new Exit(w, h);
            final ImageIcon[] icons = exit.getIcons();
            Sprite cs = vgIf.registerSprite(icons);
            cs.moveTo(x, y);
            spriteFarm.moveTo(exit, x, y);
        }
    }

    @Override
    public synchronized void createTrap(int x, int y, int w, int h) {
        if(state==MainState.BUILDING_LEVEL) {
            final Trap trap = new Trap(w, h);
            final ImageIcon[] icons = trap.getIcons();
            Sprite cs = vgIf.registerSprite(icons);
            cs.moveTo(x, y);
            spriteFarm.moveTo(trap, x, y);
        }
    }

    @Override
    public synchronized void createGhost(int w, int h, int pxs[], int pys[], int speed) {
        if(state==MainState.BUILDING_LEVEL) {
            final int x = pxs[0];
            final int y = pys[0];
            final Ghost ghost = new Ghost(w, h);
            final ImageIcon[] icons = ghost.getIcons();
            final Sprite cs = vgIf.registerSprite(icons);
            cs.moveTo(x, y);
            spriteFarm.moveTo(ghost, x, y);
            tickListenerSet.add(
                new TickListener() {
                    @Override
                    public void tick() {
                        cs.setImage(ghost.getNextFrame());
                    }
                }
                , 7);

            tickListenerSet.add(
                new TickListener() {
                    private final double tvl = 5;
                    private final int[] pointsx = pxs;
                    private final int[] pointsy = pys;
                    private double currentX = pointsx[0];
                    private double currentY = pointsy[0];
                    private double deltaX;
                    private double deltaY;
                    private int pointCounter = 0;

                    @Override
                    public void tick() {
                        final int ni = (pointCounter + 1) % pointsx.length;
                        final double nextX = pointsx[ni];
                        final double nextY = pointsy[ni];
                        final double dx = nextX - pointsx[pointCounter];
                        final double dy = nextY - pointsy[pointCounter];
                        final double d = Math.sqrt(dx*dx + dy*dy);
                        final double r = tvl / d;
                        final double tvx = r * dx;
                        final double tvy = r * dy;
                        currentX = currentX + tvx;
                        currentY = currentY + tvy;
                        final double newdx = currentX - pointsx[pointCounter];
                        final double newdy = currentY - pointsy[pointCounter];
                        if((Math.abs(newdx)>Math.abs(dx))||(Math.abs(newdy)>Math.abs(dy))) {
                            pointCounter = (pointCounter + 1) % pointsx.length;
                            currentX = nextX;
                            currentY = nextY;
                        } else {
                            spriteFarm.moveTo(ghost, currentX, currentY);
                            cs.moveTo(currentX, currentY);
                        }
                    }
                }
                , speed);
        }
    }

    @Override
    public synchronized void registerTickHandler(Object o) {
        ;
    }

    @Override
    public synchronized void registerCollisionHandler(Object o) {
        ;
    }
}

interface InternalEvents {
    void playerDied();

    void levelCompleted();

    String initLevel(String s);

    void gameOver(boolean victory);
}

