import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class LightSliderUI extends BasicSliderUI {


    public LightSliderUI(JSlider b) {
        super(b);
    }

    private Shape createThumbShape(int width, int height) {
        Ellipse2D shape = new Ellipse2D.Double(0, 0, width, height);
        return shape;
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2f));
        g2d.setPaint(Color.LIGHT_GRAY);
        Color oldColor = Color.LIGHT_GRAY;
        Rectangle trackBounds = trackRect;
        if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
            g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2,
                    trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
            int lowerX = thumbRect.width / 2;
            int upperX = thumbRect.x + (thumbRect.width / 2);
            int cy = (trackBounds.height / 2) - 2;
            g2d.translate(trackBounds.x, trackBounds.y + cy);
            g2d.setColor( Color.red);
            g2d.drawLine(lowerX - trackBounds.x, 2, upperX - trackBounds.x, 2);
            g2d.translate(-trackBounds.x, -(trackBounds.y + cy));
            g2d.setColor(oldColor);
        }
        g2d.setStroke(old);
    }


    /**
     * Overrides superclass method to do nothing.  Thumb painting is handled
     * within the <code>paint()</code> method.
     */
    @Override
    public void paintThumb(Graphics g) {
        Rectangle knobBounds = thumbRect;
        int w = knobBounds.width;
        int h = knobBounds.height;
        Graphics2D g2d = (Graphics2D) g.create();
        Shape thumbShape = createThumbShape(w - 1, h - 1);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(knobBounds.x, knobBounds.y);
        g2d.setColor(Color.WHITE);
        g2d.fill(thumbShape);
        g2d.setColor(Color.BLUE);
        g2d.draw(thumbShape);
        g2d.dispose();
    }

}