package szoftverfolyamat.osz.menu;

import szoftverfolyamat.osz.game.controller.EventProcessor;
import szoftverfolyamat.osz.game.view.GamePanel;
import szoftverfolyamat.osz.commons.graphics.GenericPanel;
import szoftverfolyamat.osz.commons.graphics.GenericButton;
import szoftverfolyamat.osz.commons.graphics.GenericLabel;
import szoftverfolyamat.osz.commons.graphics.GraphicConstants;
import szoftverfolyamat.osz.commons.graphics.AreYouSureDialog;
import szoftverfolyamat.osz.commons.graphics.ModalDialog;
import szoftverfolyamat.osz.commons.graphics.MessageBox;
import szoftverfolyamat.osz.commons.ServiceAllocator;
import szoftverfolyamat.osz.model.Configuration;
import szoftverfolyamat.osz.model.MenuCallsDao;
import szoftverfolyamat.osz.model.Mission;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.util.Timer;
import java.util.TimerTask;

public class Launcher {
    public static void main(String[] sa) {
        Configuration configuration =
            ServiceAllocator.allocateService(Configuration.class);
        int wHeight = configuration.getWindowHeight();
        int wWidth = configuration.getWindowWidth();
        if((wWidth<384)||(wHeight<256)) {
            wWidth = 800;
            wHeight = 600;
            configuration.setWindowWidth(wWidth);
            configuration.setWindowHeight(wHeight);
        }
        String lang = configuration.getLanguage();
        if(lang==null) {
            lang = "HU";
            configuration.setLanguage(lang);
        }

        final MenuCallsDao mcd = ServiceAllocator.allocateService(MenuCallsDao.class);
        mcd.getMissions();

        final MainWindow mw = new MainWindow(wWidth, wHeight, mcd);
        mw.setMainMenu();
        GraphicConstants.invokeLater(
            new Runnable() {
                public void run() {
                    mw.pack();
                    mw.setVisible(true);
                }
            }
        );
        ModalDialog.jframe = mw;
    }
}

class HighScoresPanel extends GenericPanel {
    public HighScoresPanel() {
        super(GraphicConstants.STANDARD_BG_COLOR_INT);
        setEmptyBorder(40);
        setBorderLayout();

        JTextArea jta = new JTextArea();
        jta.setFont(GraphicConstants.BIG_MONOSPACE_BOLD_FONT);
        jta.setEditable(false);
        jta.setBackground(GraphicConstants.STANDARD_BG_COLOR);
        jta.setForeground(GraphicConstants.LIGHT_TEXT_COLOR);
        jta.setText(" * * *  H A L L   OF   F A M E  * * *\n\n" +
                    " 1. EGYIK_JATEKOS        876543\n" +
                    " 2. MASIK_JATEKOS        765432\n" +
                    " 3. HARMADIK_JATEKOS     654322\n" +
                    " 4. EGYIK_JATEKOS        555555\n" +
                    " 5. MASIK_JATEKOS        543211\n" +
                    " 6. HARMADIK_JATEKOS     512121\n" +
                    " 7. EGYIK_JATEKOS        432155\n" +
                    " 8. MASIK_JATEKOS        343434\n" +
                    " 9. EGYIK_JATEKOS        321321\n" +
                    "10. MASIK_JATEKOS        121212\n");
        addToCenter(jta);
    }
}

class ButtonBar extends GenericPanel {
    final MainWindow mainWindow;

    public ButtonBar(MainWindow mw) {
        super(GraphicConstants.STANDARD_BG_COLOR_INT);
        mainWindow = mw;
        setBoxLayout(false);
        add(new MenuButton("Jatek betoltese") {
                @Override
                protected void onClick() {
                    MessageBox mb =
                        new MessageBox("Jatek betoltese", 300, 150) {
                            @Override
                            protected void clicked() {
                                dropDialog();
                            }
                        };
                    mb.setText("Ez a funkcio meg nem elerheto...");
                    mb.setVisibleLater();
                }
            }
           );
        addSpacer();
        add(new MenuButton("Uj jatek inditasa") {
                @Override
                protected void onClick() {
                    MessageBox mb =
                        new MessageBox("Uj jatek inditasa", 300, 150) {
                            @Override
                            protected void clicked() {
                                dropDialog();
                                BogusPi pi = new BogusPi(3);
                                pi.setVisible(true);
                                mainWindow.setGaming();
                            }
                        };
                    mb.setText("Itt tudja majd kivalasztani\n" +
                               "a palyat, amin jatszani fog.");
                    mb.setVisibleLater();
                }
            }
           );
        addSpacer();
        add(new MenuButton("Beallitasok") {
                @Override
                protected void onClick() {
                    MessageBox mb =
                        new MessageBox("A jatek beallitasai", 300, 150) {
                            @Override
                            protected void clicked() {
                                dropDialog();
                            }
                        };
                    mb.setText("A kulonbozo beallitasok kerulnek ide.\n" +
                               "Meg nem latszik, hogy mik lesznek ezek.");
                    mb.setVisibleLater();
                }
            }
           );
        addSpacer();
        add(new MenuButton("Sugo") {
                @Override
                protected void onClick() {
                    MessageBox mb =
                        new MessageBox("Sugo",
                                       300, 150) {
                            @Override
                            protected void clicked() {
                                dropDialog();
                            }
                        };
                    mb.setText("A sugo ablak es a\n nevjegy kerulnek ide.");
                    mb.setVisibleLater();
                }
            }
           );
        addSpacer();
        add(new MenuButton("Kilepes") {
                @Override
                protected void onClick() {
                    AreYouSureDialog aysd =
                        new AreYouSureDialog("Biztosan kilep?", 300, 150) {
                            @Override
                            protected void yesClicked() {
                                dropDialog();
                                System.exit(0);
                            }
                        };
                    aysd.setText("Biztosan meg akarja szakitani\na jatekot?");
                    aysd.setVisibleLater();
                }
            }
           );
    }

    void addSpacer() {
        add(new GenericLabel(" "));
    }
}

abstract class MenuButton extends GenericButton {
    public MenuButton(String c) {
        super(c, GraphicConstants.BUTTON_COLOR_INT,
              GraphicConstants.BUTTON_HILITE_COLOR_INT,
              GraphicConstants.LIGHT_TEXT_COLOR_INT);
        setFont(GraphicConstants.STANDARD_FONT);
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}

class MainWindow extends JFrame implements GameCtrlCallsMenu {
    private GenericPanel contentPanel;
    private final MainWindow thiz = this;
    private final MenuCallsDao menuCallsDao;

    public MainWindow(int w, int h, MenuCallsDao mcd) {
        super("Jatekprogram");
        setSize(new Dimension(w, h));
        setPreferredSize(new Dimension(w, h));
        setMinimumSize(new Dimension(800, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation(200, 100);
        setBackground(GraphicConstants.STANDARD_BG_COLOR);
        menuCallsDao = mcd;
    }

    private void mopup() {
        if(contentPanel!=null) {
            remove(contentPanel);
        }
        contentPanel = new GenericPanel(GraphicConstants.STANDARD_BG_COLOR_INT);
        contentPanel.setEmptyBorder(6);
        contentPanel.setBorderLayout();
        add(contentPanel, BorderLayout.CENTER);
    }

    void setMainMenu() {
        GraphicConstants.invokeLater(
            new Runnable() {
                public void run() {
                    mopup();
                    ButtonBar bb = new ButtonBar(thiz);
                    contentPanel.addToLeft(bb);
                    HighScoresPanel hsp = new HighScoresPanel();
                    contentPanel.addToCenter(hsp);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
            }
        );
    }

    void setGaming() {
        GraphicConstants.invokeLater(
            new Runnable() {
                public void run() {
                    mopup();
                    GamePanel gp = new GamePanel();
                    EventProcessor ep = new EventProcessor(thiz);
                    ep.setViewInterfaces(gp, gp, gp);
                    gp.setControlInterfaces(ep, ep);
                    contentPanel.add(gp, BorderLayout.CENTER);
                    Mission m = menuCallsDao.getMissions().iterator().next();
                    ep.start(m, "player's name", thiz);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
            }
        );
    }

    @Override
    public void backToMainMenu() {
        setMainMenu();
    }

    @Override
    public void loadGame() {
        System.err.println("Exiting instead of loading a game.");
        System.exit(0);
    }

    @Override
    public void startMission() {
        System.err.println("Exiting instead of starting new game.");
        System.exit(0);
    }

    @Override
    public void error(String s) {
        System.err.println(s);
        System.exit(-1);
    }
}

class BogusPi extends ModalDialog {
    private int secs;

    public BogusPi(int s) {
        super("Kerem varjon....", 150, 90);
        setModal(false);
        secs = s;
        final Timer timer = new Timer();
        setText("||");
        TimerTask task =
            new TimerTask() {
                public void run() {
                    secs--;
                    if(secs==0) {
                        timer.cancel();
                        dropDialog();
                    } else {
                        setText(getText() + "|");
                        repaint();
                    }
                }
            };
        timer.schedule(task, 0, 1000);
    }
}