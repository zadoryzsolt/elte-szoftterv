package szoftverfolyamat.osz.game.view;

import szoftverfolyamat.osz.game.controller.ViewControlsCallsGameCtrl;
import szoftverfolyamat.osz.game.controller.ViewTerminalCallsGameCtrl;
import szoftverfolyamat.osz.commons.graphics.GenericButton;
import szoftverfolyamat.osz.commons.graphics.AreYouSureDialog;
import szoftverfolyamat.osz.commons.ServiceAllocator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Component;

class RestartLevelButton extends TerminalButton {
    public RestartLevelButton() {
        super("Palya ujrakezdese", null);
    }

    @Override
    protected void onClick() {
        AreYouSureDialog aysd =
            new AreYouSureDialog("Biztosan ujrakezdi?", 300, 150) {
                @Override
                protected void yesClicked() {
                    dropDialog();
                    gci.restartLevel();
                }
            };
        aysd.setText("Biztosan ujrakezdi a palyat?");
        aysd.setVisibleLater();
    }
}

class BackToMainMenuButton extends TerminalButton {
    public BackToMainMenuButton() {
        super("Jatek megszakitasa", null);
    }

    @Override
    protected void onClick() {
        AreYouSureDialog aysd =
            new AreYouSureDialog("Biztosan kilep?", 300, 150) {
                @Override
                protected void yesClicked() {
                    dropDialog();
                    gci.abortGame();
                }
            };
        aysd.setText("Biztosan meg akarja szakitani\na jatekot?");
        aysd.setVisibleLater();
    }
}

class PauseButton extends TerminalButton {
    private boolean paused;

    public PauseButton() {
        super("     Szunet     ", null);
        paused = false;
    }

    @Override
    protected void onClick() {
        gci.pause();
        if(!paused) {
            setText("Szunet vege");
            paused = true;
        } else {
            setText("     Szunet     ");
            paused = false;
        }
    }
}

class CommitCodeButton extends TerminalButton {
    public CommitCodeButton(ScrollableEditorPane sep) {
        super("Kod futtatasa", sep);
    }

    @Override
    protected void onClick() {
        gti.executeCode(scrollableEditorPane.getSource());
    }
}

class QuitTerminalButton extends TerminalButton {
    public QuitTerminalButton(ScrollableEditorPane sep) {
        super("Kilepes a terminalbol", sep);
    }

    @Override
    protected void onClick() {
        gti.exitTerminal();
    }
}

//------------------------------------------------------------------------------

class GameAreaKeyAdapter extends KeyAdapter {
    private final char NORTH_KEY = 'w';
    private final char SOUTH_KEY = 's';
    private final char WEST_KEY = 'a';
    private final char EAST_KEY = 'd';
    private final char USE_KEY = 'g';

    private byte keysDown;
    private byte prevKeysDown;
    private ViewControlsCallsGameCtrl gci;

    public GameAreaKeyAdapter() {
        //           nsew
        keysDown = 0b0000;
        prevKeysDown = 0b0000;
    }

    void setControllerIf(ViewControlsCallsGameCtrl i) {
        gci = i;
    }

    void focusRestored() {
        stopMoving();
    }

    void stopMoving() {
        keysDown = 0b0000;
        prevKeysDown = 0b0000;
        gci.movePlayer(ViewControlsCallsGameCtrl.Direction.NIL);
    }

    @Override
    public void keyPressed(KeyEvent e)  {
        final char k = e.getKeyChar();
        boolean cont = false;
        prevKeysDown = keysDown;
        if(k==USE_KEY) {
            gci.activate();
        } else if(k==NORTH_KEY) {
            keysDown |= 0b1000;
            cont = true;
        } else if(k==SOUTH_KEY) {
            keysDown |= 0b0100;
            cont = true;
        } else if(k==WEST_KEY) {
            keysDown |= 0b0001;
            cont = true;
        } else if(k==EAST_KEY) {
            keysDown |= 0b0010;
            cont = true;
        }

        if(cont && (prevKeysDown!=keysDown)) {
            ViewControlsCallsGameCtrl.Direction direction =
                ViewControlsCallsGameCtrl.Direction.NIL;
            if(!areThreeOrMoreBitSet(keysDown)) {
                direction = getDirectionForByte(keysDown);
                if(direction==null) {
                    if(keysDown==0b1100) {
                        if(prevKeysDown==0b1000) {
                            direction = ViewControlsCallsGameCtrl.Direction.S;
                        } else if(prevKeysDown==0b0100) {
                            direction = ViewControlsCallsGameCtrl.Direction.N;
                        }
                    } else if(keysDown==0b0011) {
                        if(prevKeysDown==0b0001) {
                            direction = ViewControlsCallsGameCtrl.Direction.E;
                        } else if(prevKeysDown==0b0010) {
                            direction = ViewControlsCallsGameCtrl.Direction.W;
                        }
                    }
                }
            }
            gci.movePlayer(direction);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        final char k = e.getKeyChar();
        boolean cont = false;
        if(k==NORTH_KEY) {
            keysDown &= 0b0111;
            cont = true;
        } else if(k==SOUTH_KEY) {
            keysDown &= 0b1011;
            cont = true;
        } else if(k==WEST_KEY) {
            keysDown &= 0b1110;
            cont = true;
        } else if(k==EAST_KEY) {
            keysDown &= 0b1101;
            cont = true;
        }
        if(cont) {
            if(!areThreeOrMoreBitSet(keysDown)) {
                if((keysDown!=0b1100)&&(keysDown!=0b0011)) {
                    ViewControlsCallsGameCtrl.Direction direction =
                        getDirectionForByte(keysDown);
                    gci.movePlayer(direction);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e)  {}

    private boolean areThreeOrMoreBitSet(byte b) {
        return (b==0b1111)||(b==0b1011)||(b==0b1101)||(b==0b1110)||(b==0b0111);
    }

    private ViewControlsCallsGameCtrl.Direction getDirectionForByte(byte b) {
        ViewControlsCallsGameCtrl.Direction ret = null;
        if(b==0b1000) {
            ret = ViewControlsCallsGameCtrl.Direction.N;
        } else if(b==0b0001) {
            ret = ViewControlsCallsGameCtrl.Direction.W;
        } else if(b==0b0100) {
            ret = ViewControlsCallsGameCtrl.Direction.S;
        } else if(b==0b0010) {
            ret = ViewControlsCallsGameCtrl.Direction.E;
        } else if(b==0b0101) {
            ret = ViewControlsCallsGameCtrl.Direction.SW;
        } else if(b==0b1010) {
            ret = ViewControlsCallsGameCtrl.Direction.NE;
        } else if(b==0b1001) {
            ret = ViewControlsCallsGameCtrl.Direction.NW;
        } else if(b==0b0110) {
            ret = ViewControlsCallsGameCtrl.Direction.SE;
        } else if(b==0b0000) {
            ret = ViewControlsCallsGameCtrl.Direction.NIL;
        }
        return ret;
    }
}
