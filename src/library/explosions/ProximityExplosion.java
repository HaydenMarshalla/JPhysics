package library.explosions;

import library.dynamics.Body;
import testbed.Camera;
import library.utils.ColourSettings;
import library.dynamics.World;
import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class ProximityExplosion extends Explosions {
    private final ArrayList<Vectors2D> linesToBodies = new ArrayList<>();
    private final int proximity;

    public ProximityExplosion(Vectors2D centrePoint, int radius) {
        super(centrePoint);
        proximity = radius;
    }

    public synchronized void updateProximity(ArrayList<Body> bodiesToEvaluate) {
        linesToBodies.clear();
        bodiesEffected.clear();
        for (Body b : bodiesToEvaluate) {
            Vectors2D blastDist = b.position.subtract(epicentre);
            if (blastDist.length() <= proximity) {
                bodiesEffected.add(b);
                linesToBodies.add(b.position);
            }
        }
    }

    public synchronized void draw(Graphics g, ColourSettings paintSettings, Camera camera) {
        Graphics2D gi = (Graphics2D) g;
        gi.setColor(paintSettings.proximity);
        Vectors2D circlePotion = camera.scaleToScreen(epicentre);
        double proximityRadius = camera.scaleToScreenXValue(proximity);
        gi.draw(new Ellipse2D.Double(circlePotion.x - proximityRadius, circlePotion.y - proximityRadius, 2 * proximityRadius, 2 * proximityRadius));

        for (Vectors2D p : linesToBodies) {
            g.setColor(paintSettings.proximity);
            Vectors2D worldCoord = camera.scaleToScreen(p);
            gi.draw(new Line2D.Double(circlePotion.x, circlePotion.y, worldCoord.x, worldCoord.y));

            g.setColor(paintSettings.linesToObjects);
            double lineToRadius = camera.scaleToScreenXValue(paintSettings.CIRCLE_RADIUS);
            gi.fill(new Ellipse2D.Double(worldCoord.x - lineToRadius, worldCoord.y - lineToRadius, 2 * lineToRadius, 2 * lineToRadius));
        }
    }
}
