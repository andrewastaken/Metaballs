import javax.swing.JFrame;

public class MyFrame extends JFrame {
    public MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.add(new MyPanel());
        this.pack();
    }
}
