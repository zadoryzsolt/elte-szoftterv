package untrusted;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptContext;

public class Untrusted {
    public static void main(String[] sa) {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }
}

abstract class MyButton extends JButton {
    public MyButton(String s) {
        super(s);
        addActionListener(new ActionListener() {
                              public void actionPerformed(ActionEvent e) {
                                  onClick();
                              }
                          }
                         );
    }

    abstract void onClick();
}

class MainWindow extends JFrame {
    public MainWindow() {
        super("JavaScript futtatas");
        setSize(new Dimension(600, 400));
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        final ScrollableTextArea codePanel = new ScrollableTextArea();
        codePanel.setPreferredSize(new Dimension(400, 250));
        codePanel.setEditable(true);
        codePanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        final TestPanel testPanel = new TestPanel();
        testPanel.setPreferredSize(new Dimension(200, 200));

        final JSplitPane upperSplit =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, testPanel, codePanel);
        upperSplit.setResizeWeight(0.5);

        final ScrollableTextArea errorConsole = new ScrollableTextArea();

        final JSplitPane mainPanel =
            new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplit, errorConsole);
        mainPanel.setResizeWeight(0.7);
        add(mainPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 8, 8));
        buttonPanel.add(new MyButton("Kod futtatasa") {
                            void onClick() {
                                JavascriptHost javascriptHost = new JavascriptHost();
                                javascriptHost.addReference("balPanel", testPanel);
                                errorConsole.setText(javascriptHost.runScript(codePanel.getText()));
                            }
                        }
                       );
        buttonPanel.add(new MyButton("Kod torlese") {
                            void onClick() {
                                codePanel.clear();
                            }
                        }
                       );
        buttonPanel.add(new MyButton("Hibakonzol torlese") {
                            void onClick() {
                                errorConsole.clear();
                            }
                        }
                       );
        buttonPanel.add(new MyButton("Kilepes") {
                            void onClick(){
                                System.exit(0);
                            }
                        }
                       );
        add(buttonPanel, BorderLayout.PAGE_END);
        pack();
        setLocation(200, 200);
    }
}

class ScrollableTextArea extends JScrollPane {
    private final JTextArea textArea;

    public ScrollableTextArea() {
        super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
              JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        setMinimumSize(new Dimension(100, 100));
        textArea = new JTextArea();
        setViewportView(textArea);
        setEditable(false);
    }

    void clear() {
        setText("");
    }

    void setText(String s) {
        textArea.setText(s);
    }

    String getText() {
        return textArea.getText();
    }

    void setEditable(boolean b) {
        textArea.setEditable(b);
    }

    public void setFont(Font f) {
        if(textArea!=null)
        {
            textArea.setFont(f);
        }
    }
}

class JavascriptHost {
    private final ScriptEngine engine;

    public JavascriptHost() {
        ScriptEngineManager sem = new ScriptEngineManager();
        engine = sem.getEngineByName("JavaScript");
    }

    void addReference(String s, Object o) {
        //engine.put(s, o);  // mi a kulonbseg?
        engine.getContext().setAttribute(s, o, ScriptContext.ENGINE_SCOPE);
    }

    void removeReference(String s) {
        engine.getContext().removeAttribute(s, ScriptContext.ENGINE_SCOPE);
    }

    String runScript(String s) {
        String ret = "";
        try
        {
            engine.eval(s);
        }
        catch(Exception e)
        {
            ret = " " + e.toString() + "\n";
            StackTraceElement[] stea = e.getStackTrace();
            for(StackTraceElement ste : stea)
            {
                ret = ret + "\n " + ste.toString();
            }
        }
        return ret;
    }
}