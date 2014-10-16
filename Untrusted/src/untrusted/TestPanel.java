package untrusted;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class TestPanel extends JPanel {
    private final JLabel label;
    
    public TestPanel() {
        setBackground(Color.GRAY);
        setOpaque(true);
        setMinimumSize(new Dimension(100, 100));
        setLayout(new BorderLayout());
        label = new JLabel("Text");
        add(label, BorderLayout.CENTER);
    }
    
    public void setColor(int r, int g, int b) {
        setBackground(new Color(r, g, b));
    }
    
    public void setText(String s) {
        label.setText(s);
    }
    
    public String getText() {
        return label.getText();
    }
    
    public void clear() {
        setText("");
    }
}
