package display;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
    public MyPanel() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.BLACK);
    }
}
