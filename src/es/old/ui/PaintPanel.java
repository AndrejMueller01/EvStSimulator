package es.old.ui;

import es.old.EsPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Andrej
 */
public class PaintPanel extends JPanel {

    private EsPoint targetPoint;
    private ArrayList<EsPoint> points;

    private boolean targetExists = false;
    private final int radius = 8;

    public PaintPanel() {
        super();
        this.setBackground(Color.LIGHT_GRAY);
        points = new ArrayList<>();
        initMouseListener();
        targetPoint = null;
    }

    private void initMouseListener() {
        addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent event) {
                        targetPoint = new EsPoint(event.getX(), event.getY());
                        targetExists = true;
                        repaint();
                    }
                }
        );
    }

    public boolean isTargetSet() {
        return targetExists;
    }

    public EsPoint getTargetPoint() {
        return targetPoint;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // clears drawing area

        if (targetExists) {
            g.setColor(Color.RED);
            g.fillOval(targetPoint.getX() - radius / 2, targetPoint.getY() - radius / 2, radius, radius);
            g.setColor(Color.BLACK);
            g.drawString("TargetPoint", targetPoint.getX(), targetPoint.getY() - 3 * radius);
            g.drawString("x=" + targetPoint.getX() + " y=" + targetPoint.getY(),
                    targetPoint.getX(), targetPoint.getY() - radius);
        }
        
        for (EsPoint point : points) {
            g.fillOval(point.getX() - radius / 2, point.getY() - radius / 2, radius, radius);
        }
    }

    public void setPointsToDraw(ArrayList<EsPoint> points) {
        this.points = points;
    }
}
