package szoftverfolyamat.osz.game.view;

import szoftverfolyamat.osz.commons.graphics.GenericPanel;
import szoftverfolyamat.osz.commons.graphics.GraphicConstants;
import szoftverfolyamat.osz.commons.graphics.MessageBox;
import szoftverfolyamat.osz.commons.graphics.ModalDialog;
import szoftverfolyamat.osz.game.view.GameCtrlCallsViewInfoPanel;
import szoftverfolyamat.osz.game.view.GameCtrlCallsViewGraphics;
import szoftverfolyamat.osz.game.view.GameCtrlCallsViewLifecycle;
import szoftverfolyamat.osz.game.controller.ViewControlsCallsGameCtrl;
import szoftverfolyamat.osz.game.controller.ViewTerminalCallsGameCtrl;
import szoftverfolyamat.osz.model.SourceFragment;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class GamePanel extends GenericPanel
            implements GameCtrlCallsViewLifecycle, GameCtrlCallsViewGraphics,
    GameCtrlCallsViewInfoPanel, MenuCallsView {

    private final InfoPanel infoPanel;
    private final GameArea gameArea;
    private final TerminalPanel terminalPanel;
    private final GameAreaKeyAdapter gaka;
    private ViewControlsCallsGameCtrl gccIf;
    private boolean terminalActiveBeforePause;

    public GamePanel() {
        super();
        setBorderLayout();
        infoPanel = new InfoPanel();
        add(infoPanel, BorderLayout.SOUTH);
        gaka = new GameAreaKeyAdapter();
        gameArea = new GameArea(gaka);
        terminalPanel = new TerminalPanel();
        final JSplitPane upperSplit =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameArea, terminalPanel);
        upperSplit.setResizeWeight(1);
        add(upperSplit, BorderLayout.CENTER);
        ModalDialog.jframe.addWindowFocusListener(
            new WindowAdapter() {
                public void windowGainedFocus(WindowEvent e) {
                    if(terminalPanel.isActive()) {
                        enterTerminal();
                    } else {
                        exitTerminal();
                    }
                }
            }
        );
    }

    public void setControlInterfaces(ViewTerminalCallsGameCtrl c1,
                                     ViewControlsCallsGameCtrl c2) {
        terminalPanel.setControllerIf(c1);
        infoPanel.setControllerIf(c2);
        gccIf = c2;
        gaka.setControllerIf(c2);
    }

    /*
    MenuCallsView impl
    */
    public void resizePanel(int dx, int dy) {}

    /*
    GameCtrlCallsViewLifecycle impl
    */
    @Override
    public void enterTerminal() {
        gameArea.setActive(false);
        terminalPanel.setActive(true);
    }


    @Override
    public void exitTerminal() {
        terminalPanel.setActive(false);
        gameArea.setActive(true);
    }

    @Override
    public void exit() {}

    @Override
    public void pause() {
        terminalActiveBeforePause = terminalPanel.isActive();
        terminalPanel.setActive(false);
        gameArea.setActive(false);
    }

    @Override
    public void resume() {
        if(terminalActiveBeforePause) {
            enterTerminal();
        } else {
            exitTerminal();
        }
    }

    @Override
    public void playerDied(String s) {
        MessageBox mb =
            new MessageBox("Halal!", 300, 150) {
                @Override
                protected void clicked() {
                    dropDialog();
                }
            };
        mb.setText("Maga meghalt.");
        mb.setVisibleLater();
    }

    @Override
    public void defeat(String s) {
        MessageBox mb =
            new MessageBox("Jatek vege\n" + s, 300, 150) {
                @Override
                protected void clicked() {
                    dropDialog();
                }
            };
        mb.setText("Vege a jateknak, maga kikapott!");
        mb.setVisibleLater();
    }

    @Override
    public void victory(String s) {
        MessageBox mb =
            new MessageBox("Feladat teljesitve", 300, 150) {
                @Override
                protected void clicked() {
                    dropDialog();
                    gameAborted();
                }
            };
        mb.setText("Vege a jateknak, maga nyert!");
        mb.setVisibleLater();

    }

    @Override
    public void levelCompleted(String s) {
        MessageBox mb =
            new MessageBox("Szint teljesitve", 300, 150) {
                @Override
                protected void clicked() {
                    dropDialog();
                    gccIf.levelCompletionConfirmed();
                }
            };
        mb.setText("Teljesitette a szintet!");
        mb.setVisibleLater();
    }

    @Override
    public void gameAborted() {
        final GamePanel t = this;
        MessageBox mb =
            new MessageBox("Jatek megszakitva", 300, 150) {
                @Override
                protected void clicked() {
                    dropDialog();
                    t.removeAll();
                    t.repaint();
                    gccIf.gameoverConfirmed(ViewControlsCallsGameCtrl.BACK_TO_MAIN_MENU);
                }
            };
        mb.setText("Ujat tolt be,\nujat indit,\nvagy vissza a fomenube?");
        mb.setVisibleLater();
    }

    @Override
    public void setSource(Iterable<SourceFragment> src) {
        terminalPanel.setSource(src);
    }

    /*
    GameCtrlCallsViewGraphics
    */
    @Override
    public void initStage(int w, int h, int color) {
        gameArea.initStage(w, h, color);
    }

    @Override
    public Sprite registerSprite(double w, double h, Font f, String s) {
        return null;
    }

    @Override
    public Sprite registerSprite(ImageIcon[] imga) {
        return gameArea.registerSprite(imga);
    }

    @Override
    public void moveCanvas(double x, double y) {
        gameArea.moveCanvas(y, y);
    }

    /*
    GameCtrlCallsViewInfoPanel
    */
    @Override
    public void setPlayerInfo(String s) {
        infoPanel.setPlayerInfo(s);
    }

    @Override
    public void setLevelInfo(String s) {
        infoPanel.setLevelInfo(s);
    }

    @Override
    public void setTime(String hour, String min, String sec) {
        infoPanel.setTime(hour, min, sec);
    }

    @Override
    public void setScore(String s) {
        infoPanel.setScore(s);
    }

    @Override
    public void gameSaved(String s) {
        infoPanel.gameSaved(s);
    }

    @Override
    public void runtimeError(String s) {
        infoPanel.runtimeError(s);
    }
    
    @Override
    public void ping() {
        infoPanel.ping();
    }
}



