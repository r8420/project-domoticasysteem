import javax.swing.*;
import java.awt.*;

public class LichtGraphicsPanel extends JPanel {

    private float lichtsterkte;

    public LichtGraphicsPanel() {
        this.lichtsterkte = 1f;
    }

    public void setLichtsterkte(int lichtsterkte) {
        this.lichtsterkte = lichtsterkte/10f;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int diameter = Math.min(getHeight(), getWidth());
        int x = diameter/4;
        int y = diameter/8;
        Color achtergrond = new Color(255, 205, 214);

        setBackground(achtergrond);

        g.setColor(Color.YELLOW);

        g.fillOval(0, 0, diameter, diameter); // grote gele cirkel

        Color transparanteCirkel = new Color(1f, 1f,0f, lichtsterkte);

        g.setColor(achtergrond);
        g.fillOval(x, y, diameter-x, diameter-2*y); // roze achtergrond waardoor dit gedeelte transparant lijkt
        g.setColor(transparanteCirkel);
        g.fillOval(x, y, diameter-x, diameter-2*y);

    }
}
