package raycasts;

import library.Body;
import library.Camera;
import library.ColourSettings;
import library.World;
import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class ProximityExplosion extends RayCast {
    public ArrayList<Body> inRangeBodies = new ArrayList<>();
    private ArrayList<Vectors2D> linesToBodies = new ArrayList<>();
    private int proximity;

    public ProximityExplosion(Vectors2D centrePoint, World world, int radius) {
        super(centrePoint, world);
        proximity = radius;
        world.addRaycastObject(this);
    }

    @Override
    public void update() {
        proximityCheck();
    }

    public synchronized void proximityCheck() {
        linesToBodies.clear();
        inRangeBodies.clear();
        for (Body b : world.bodies) {
            Vectors2D blastDist = b.position.subtract(epicentre);
            if (blastDist.length() <= proximity) {
                inRangeBodies.add(b);
                linesToBodies.add(b.position);
            }
        }
    }

    @Override
    public void applyBlastImpulse(double blastPower) {
        for (Body b : inRangeBodies) {
            Vectors2D blastDir = b.position.subtract(epicentre);
            double distance = blastDir.length();
            if (distance == 0) return;

            double invDistance = 1 / distance;
            double impulseMag = blastPower * invDistance;

            Vectors2D force = b.force.addi(blastDir.normalize().scalar(impulseMag));
            b.velocity.add(force.scalar(b.invMass));
        }
    }

    @Override
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
