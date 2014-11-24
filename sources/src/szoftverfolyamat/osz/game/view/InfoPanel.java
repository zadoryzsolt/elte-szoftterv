package szoftverfolyamat.osz.game.view;

import szoftverfolyamat.osz.commons.graphics.GenericPanel;
import szoftverfolyamat.osz.commons.graphics.GenericLabel;
import szoftverfolyamat.osz.commons.graphics.GraphicConstants;
import szoftverfolyamat.osz.commons.graphics.MessageBox;
import szoftverfolyamat.osz.game.controller.ViewControlsCallsGameCtrl;

class InfoPanel extends GenericPanel
    implements GameCtrlCallsViewInfoPanel {

    private final int TEXT_COLOR = GraphicConstants.LIGHT_TEXT_COLOR_INT;
    private final int BG_COLOR = GraphicConstants.STANDARD_BG_COLOR_INT;
    private final InfoCaptionLabel timeCaption;
    private final MonospaceLabel timeValue;
    private final InfoCaptionLabel playerCaption;
    private final InfoCaptionLabel playerInfo;
    private final InfoCaptionLabel levelCaption;
    private final InfoCaptionLabel levelValue;
    private final InfoCaptionLabel scoreCaption;
    private final MonospaceLabel scoreValue;
    private final InfoCaptionLabel messageLabel;
    private final PauseButton pauseButton;
    private final BackToMainMenuButton bmmButton;
    private final RestartLevelButton restartLevelButton;

    public InfoPanel() {
        super();
        setBackground(BG_COLOR);
        setBoxLayout(false);

        timeCaption = new InfoCaptionLabel(TEXT_COLOR, "Hatralevo ido:  ", true);
        playerCaption = new InfoCaptionLabel(TEXT_COLOR, "Jatekos:  ", true);
        levelCaption = new InfoCaptionLabel(TEXT_COLOR, "Palya:  ", true);
        scoreCaption = new InfoCaptionLabel(TEXT_COLOR, "Pontszam:    ", true);
        messageLabel = new InfoCaptionLabel(TEXT_COLOR, "  ", true);
        timeValue = new MonospaceLabel(TEXT_COLOR, "", false);
        playerInfo = new InfoCaptionLabel(TEXT_COLOR, "", false);
        levelValue = new InfoCaptionLabel(TEXT_COLOR, "", false);
        scoreValue = new MonospaceLabel(TEXT_COLOR, "", false);

        GenericPanel upanel = new GenericPanel();
        upanel.setOpaque(false);
        upanel.setBoxLayout(true);
        upanel.add(new CaptionPanel(playerCaption, playerInfo));
        upanel.add(new MonospaceLabel(0, " ", false));
        upanel.add(new CaptionPanel(scoreCaption, scoreValue));
        upanel.add(new MonospaceLabel(0, " ", false));
        upanel.add(new CaptionPanel(timeCaption, timeValue));
        upanel.add(new MonospaceLabel(0, " ", false));
        upanel.add(new CaptionPanel(levelCaption, levelValue));

        GenericPanel dpanel = new GenericPanel();
        dpanel.setOpaque(false);
        dpanel.setBoxLayout(true);
        pauseButton = new PauseButton();
        dpanel.add(pauseButton);
        dpanel.add(new MonospaceLabel(0, "  ", false));
        restartLevelButton = new RestartLevelButton();
        dpanel.add(restartLevelButton);
        dpanel.add(new MonospaceLabel(0, "  ", false));
        bmmButton = new BackToMainMenuButton();
        dpanel.add(bmmButton);


        add(upanel);
        add(new MonospaceLabel(0, " ", false));
        add(dpanel);

        setTime("00", "00", "00");
        setPlayerInfo("");
        setLevelInfo("");
        setScore("");

        setAllSizes(400, 100);
    }

    void setControllerIf(ViewControlsCallsGameCtrl i) {
        pauseButton.setControllerIf(i);
        bmmButton.setControllerIf(i);
        restartLevelButton.setControllerIf(i);
    }

    @Override
    public void setPlayerInfo(String s) {
        playerInfo.setText(s);
    }

    @Override
    public void setLevelInfo(String s) {
        levelValue.setText(s);
    }

    @Override
    public void setTime(String hour, String min, String sec) {
        timeValue.setText(hour + ":" + min + ":" + sec);
    }

    @Override
    public void setScore(String s) {
        scoreValue.setText(s);
    }

    @Override
    public void gameSaved(String s) {
        messageLabel.setText(s);
    }

    @Override
    public void ping() {
        revalidate();
        repaint();
    }
    
    @Override
    public void runtimeError(String s) {
        MessageBox mb =
            new MessageBox("Rossz a beirt kod!", 400, 400) {
                @Override
                protected void clicked() {
                    dropDialog();
                }
            };
        mb.setText(s);
        mb.setResizable(true);
        mb.getTextArea().setLineWrap(true);
        mb.getTextArea().setFont(GraphicConstants.MONOSPACE_FONT);
        mb.setVisibleLater();
    }

    class InfoCaptionLabel extends GenericLabel {
        public InfoCaptionLabel(int col, String cap, boolean b) {
            super(col, cap);
            if(b) {
                setFont(GraphicConstants.STANDARD_FONT);
            } else {
                setFont(GraphicConstants.BIG_STANDARD_BOLD_FONT);
            }
        }
    }

    class MonospaceLabel extends GenericLabel {
        public MonospaceLabel(int col, String cap, boolean b) {
            super(col, cap);
            if(b) {
                setFont(GraphicConstants.MONOSPACE_FONT);
            } else {
                setFont(GraphicConstants.BIG_MONOSPACE_BOLD_FONT);
            }
        }
    }

    class CaptionPanel extends GenericPanel {
        public CaptionPanel(GenericLabel ul, GenericLabel ll) {
            setOpaque(false);
            setBoxLayout(false);
            ul.alignToLeft();
            ll.alignToLeft();
            add(ul);
            add(ll);
        }
    }
}

