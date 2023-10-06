package Snow;

/* 
 * https://www.delftstack.com/howto/java/use-recursion-to-draw-koch-snowflake-in-java/
 * https://thecodingtrain.com/CodingChallenges/129-koch-snowflake.html
 */
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class kochCurve implements ActionListener {
    static int n = 0;
    JFrame frame = new JFrame();
    private MyCanvas canvas;

    /*
     * @desc: basic java frame setup
     */
    public kochCurve() throws InterruptedException {
        canvas = new MyCanvas(n);

        JButton add = new JButton("Next level");
        add.addActionListener(this);
        JButton reset = new JButton("Previous level");
        reset.addActionListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(800, 800));
        panel.add(canvas, BorderLayout.CENTER);
        panel.add(add, BorderLayout.NORTH);
        panel.add(reset, BorderLayout.SOUTH);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Koch Curve : " + n + " time of generation");
        frame.pack();
        frame.setVisible(true);
    }

    // button Lister, if click next level will generate 1 level up of koch curve
    // if is pervious will jump back to the previous level of koch curve
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Next level")) {
            n++;
            canvas.setLevel(n);
        } else if (command.equals("Previous level")) {
            if (n == 0) {
                JOptionPane.showMessageDialog(null, "Can't make negtive number.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                n--;
                canvas.setLevel(n);
            }
        }
        frame.setTitle("Koch Curve: " + n + " time of generation");
    }

    /*
     * main function
     */
    public static void main(String[] a) throws InterruptedException {
        new kochCurve();

    }

}

class MyCanvas extends JComponent {

    int level = 0;
    double width = 500;
    double height = 500;
    double centerX, centerY;

    // re-scale
    public MyCanvas(int level) {
        this.level = level;
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (e.getComponent() instanceof JPanel) {
                    JPanel panel = (JPanel) e.getComponent();
                    updateSize(panel.getSize());
                }
            }
        });
    }

    // time of generation
    public void setLevel(int level) {
        this.level = level;
        repaint();
    }

    // Update the current size for use later
    public void updateSize(Dimension size) {
        width = size.width;
        height = size.height;
        centerX = width / 2;
        centerY = height / 2;
        repaint();
    }

    /*
     * @param g: Graphics
     * 
     * @desc: paints all thing
     */
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // calculate the size of the equilateral triangle based on the height of the
        // panel
        double size = (Math.min(getWidth(), getHeight()) * 0.6);
        // calculate the coordinates of the three corners of the equilateral triangle
        double x1 = (getWidth() + size) / 2;
        double y1 = (getHeight() + (int) (size * Math.sqrt(3) / 2)) / 2;
        double x2 = x1 - size / 2;
        double y2 = y1 - (int) (size * Math.sqrt(3) / 2);
        double x3 = x1 - size;
        double y3 = y1;

        // draw the equilateral triangle
        DrawKoch(g2d, level, x1, y1, x2, y2);
        DrawKoch(g2d, level, x2, y2, x3, y3);
        DrawKoch(g2d, level, x3, y3, x1, y1);
    }

    /*
     * most important method in this class!!!!!!!!!!
     * 
     * @param g : graphics object
     * 
     * @param initiallevel : time of koch curve generation
     * 
     * @param a1 : x coordinate of first point
     * 
     * @param b1 : y coordinate of first point
     * 
     * @param a5 : x coordinate of second point
     * 
     * @param b5 : y coordinate of second point
     * 
     * @desc: using following math formula to identify the rest 3 points to draw the
     * line
     */
    private void DrawKoch(Graphics2D z, int level, double a1, double b1, double a5, double b5) {
        double delX, delY, a2, b2, a3, b3, a4, b4;
        if (level == 0) {
            int x1 = (int) Math.round(a1);
            int x2 = (int) Math.round(b1);
            int y1 = (int) Math.round(a5);
            int y2 = (int) Math.round(b5);
            z.drawLine(x1, x2, y1, y2);
        } else {
            delX = a5 - a1;
            delY = b5 - b1;
            a2 = a1 + delX / 3;
            b2 = b1 + delY / 3;
            a3 = (0.5 * (a1 + a5) + Math.sqrt(3) * (b1 - b5) / 6);
            b3 = (0.5 * (b1 + b5) + Math.sqrt(3) * (a5 - a1) / 6);
            a4 = a1 + 2 * delX / 3;
            b4 = b1 + 2 * delY / 3;
            DrawKoch(z, level - 1, a1, b1, a2, b2);
            DrawKoch(z, level - 1, a2, b2, a3, b3);
            DrawKoch(z, level - 1, a3, b3, a4, b4);
            DrawKoch(z, level - 1, a4, b4, a5, b5);
        }
    }
}