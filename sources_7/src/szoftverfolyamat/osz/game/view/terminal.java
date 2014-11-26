package szoftverfolyamat.osz.game.view;

import szoftverfolyamat.osz.commons.graphics.GenericPanel;
import szoftverfolyamat.osz.commons.graphics.GenericLabel;
import szoftverfolyamat.osz.commons.graphics.GenericButton;
import szoftverfolyamat.osz.commons.graphics.GraphicConstants;
import szoftverfolyamat.osz.model.SourceFragment;
import szoftverfolyamat.osz.game.controller.ViewTerminalCallsGameCtrl;
import szoftverfolyamat.osz.game.controller.ViewControlsCallsGameCtrl;

import javax.swing.text.JTextComponent;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class TerminalPanel extends GenericPanel {
    private final QuitTerminalButton quitTerminalButton;
    private final ResetCodeButton resetCodeButton;
    private final CommitCodeButton commitCodeButton;
    private final ScrollableEditorPane sep;
    private boolean active;

    public TerminalPanel() {
        super(0xf0f0f0);
        setMinimumSize(new Dimension(300, 400));
        setPreferredSize(new Dimension(300, 400));
        setMaximumSize(new Dimension(800, 1200));
        setBorderLayout();
        sep = new ScrollableEditorPane();
        add(sep, BorderLayout.CENTER);
        final GenericPanel buttonPanel =
            new GenericPanel(GraphicConstants.GRAY_1_INT);
        buttonPanel.setBoxLayout(false);
        buttonPanel.add(new GenericLabel(0, "  "));
        resetCodeButton = new ResetCodeButton(sep);
        buttonPanel.add(resetCodeButton);
        buttonPanel.add(new GenericLabel(0, "  "));
        quitTerminalButton = new QuitTerminalButton(sep);
        buttonPanel.add(quitTerminalButton);
        buttonPanel.add(new GenericLabel(0, "  "));
        commitCodeButton = new CommitCodeButton(sep);
        buttonPanel.add(commitCodeButton);
        buttonPanel.add(new GenericLabel(0, "  "));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    void setActive(boolean b) {
        active = b;
        quitTerminalButton.setEnabled(b);
        resetCodeButton.setEnabled(b);
        commitCodeButton.setEnabled(b);
        sep.setActive(b);
        requestFocusInWindow();
    }

    boolean isActive() {
        return active;
    }
    
    void setSource(Iterable<SourceFragment> s) {
        sep.setSource(s);
        resetCodeButton.setSource(s);
    }

    void setControllerIf(ViewTerminalCallsGameCtrl i) {
        commitCodeButton.setControllerIf(i);
        quitTerminalButton.setControllerIf(i);
    }
}

class ResetCodeButton extends TerminalButton {
    private Iterable<SourceFragment> src;

    public ResetCodeButton(ScrollableEditorPane sep) {
        super("Kod visszaallitasa", sep);
    }

    @Override
    protected void onClick() {
        scrollableEditorPane.setSource(src);
    }

    public void setSource(Iterable<SourceFragment> s) {
        src = s;
    }
}

abstract class TerminalButton extends GenericButton {
    protected final ScrollableEditorPane scrollableEditorPane;
    protected ViewControlsCallsGameCtrl gci;
    protected ViewTerminalCallsGameCtrl gti;

    public TerminalButton(String c, ScrollableEditorPane sep) {
        super(c, GraphicConstants.BUTTON_COLOR_INT,
              GraphicConstants.BUTTON_HILITE_COLOR_INT,
              GraphicConstants.LIGHT_TEXT_COLOR_INT);
        scrollableEditorPane = sep;
        setFont(GraphicConstants.STANDARD_FONT);
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void onClick() {
        ;
    }
    
    void setControllerIf(ViewControlsCallsGameCtrl i) {
        gci = i;
    }

    void setControllerIf(ViewTerminalCallsGameCtrl i) {
        gti = i;
    }
}

//------------------------------------------------------------------------------

class RestrictedEditFilter extends DocumentFilter {
    final private int maxRows;

    public RestrictedEditFilter(int t) {
        maxRows = t;
    }

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset,
                             String string, AttributeSet attrs) {
        final PlainDocument document = (PlainDocument)(fb.getDocument());
        final int rows = document.getDefaultRootElement().getElementCount() ;
        if(!((rows==maxRows)&&(string.contains("\n")))) {
            try {
                fb.insertString(offset, string, attrs);
            } catch(Exception e) {}
        }
    }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) {
        final PlainDocument document = (PlainDocument)(fb.getDocument());
        try {
            fb.remove(offset, length);
        } catch(Exception e) {}
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset,
                        int length, String string, AttributeSet attrs) {
        final PlainDocument document = (PlainDocument)(fb.getDocument());
        final int rows = document.getDefaultRootElement().getElementCount() ;
        if(!((rows==maxRows)&&(string.contains("\n")))) {
            try {
                fb.replace(offset, length, string, attrs);
            } catch(Exception e) {}
        }
    }
}

class ScrollableEditorPane extends JScrollPane {
    private boolean active;
    private final GenericPanel panel;
    private ArrayList<String> sources;
    private ArrayList<JTextArea> textAreas;
    private ArrayList<JTextArea> allTextAreas;

    public ScrollableEditorPane() {
        super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
              JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        setMinimumSize(new Dimension(100, 100));
        panel = new GenericPanel(GraphicConstants.GRAY_1_INT);
        panel.setBoxLayout(false);
        setViewportView(panel);
        setBackground(new Color(GraphicConstants.GRAY_1_INT));
        textAreas = new ArrayList<JTextArea>();
        allTextAreas = new ArrayList<JTextArea>();
        active = false;
    }

    String getSource() {
        String ret = "";
        for(JTextArea s : allTextAreas) {
            ret = ret + s.getText() + "\n";
        }
        return ret;
    }

    void setActive(boolean b) {
        active = b;
        for(JTextArea a : textAreas) {
            a.setEnabled(active);
        }
    }

    boolean isActive() {
        return active;
    }

    void setSource(Iterable<SourceFragment> sfa) {
        sources = new ArrayList<String>();
        textAreas = new ArrayList<JTextArea>();
        allTextAreas = new ArrayList<JTextArea>();
        panel.removeAll();
        for(SourceFragment f : sfa) {
            if(f.isVisible()) {
                final int maxRows = f.getMaxRows();
                final JTextArea textArea = new JTextArea();
                textArea.setFont(GraphicConstants.MONOSPACE_FONT);
                textArea.setForeground(GraphicConstants.LIGHT_TEXT_COLOR);
                textArea.setCaretColor(GraphicConstants.LIGHT_TEXT_COLOR);
                textArea.setText(f.getCode());
                textArea.setRows(maxRows);
                textArea.setLineWrap(false);
                boolean editable = !f.isReadonly();
                if(!editable) {
                    textArea.setBackground(new Color(0x2d2a31));
                } else {
                    textArea.setBackground(new Color(0x558866));
                    final UndoManager undoManager = new UndoManager();
                    undoManager.setLimit(50);
                    textArea.getDocument().addUndoableEditListener(undoManager);
                    final Action undoAction =
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                try {
                                    undoManager.undo();
                                } catch (Exception e) {
                                    ;
                                }
                            }
                        };
                    textArea.registerKeyboardAction(undoAction,
                                                    KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK),
                                                    JComponent.WHEN_FOCUSED);
                    PlainDocument ad = (PlainDocument)(textArea.getDocument());
                    ad.setDocumentFilter(new RestrictedEditFilter(maxRows));
                }
                panel.add(textArea);
                textArea.setEditable(editable);
                textAreas.add(textArea);
                allTextAreas.add(textArea);
            } else {
                allTextAreas.add(new JTextArea(f.getCode()));
            }
            sources.add(f.getCode());
        }
        revalidate();
        panel.invokeLater(
            new Runnable() {
                public void run() {
                    paintAll(getGraphics());
                }
            }
        );
    }
}



