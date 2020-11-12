package library.explosions;

import library.dynamics.Body;
import library.math.Vectors2D;
import testbed.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class ProximityExplosion implements Explosion {
    private final int proximity;
    private Vectors2D epicentre;

    public ProximityExplosion(Vectors2D centrePoint, int radius) {
        epicentre = centrePoint;
        proximity = radius;
    }

    public void changeEpicentre(Vectors2D v) {
        epicentre = v;
    }

    public ArrayList<Body> bodiesEffected = new ArrayList<>();

    @Override
    public void update(ArrayList<Body> bodiesToEvaluate) {
        bodiesEffected.clear();
        for (Body b : bodiesToEvaluate) {
            Vectors2D blastDist = b.position.subtract(epicentre);
            if (blastDist.length() <= proximity) {
                bodiesEffected.add(b);
            }
        }
    }

    private final ArrayList<Vectors2D> linesToBodies = new ArrayList<>();

    public void updateLinesToBody() {
        linesToBodies.clear();
        for (Body b : bodiesEffected) {
            linesToBodies.add(b.position);
        }
    }

    @Override
    public void draw(Graphics2D gi, ColourSettings paintSettings, Camera camera) {
        gi.setColor(paintSettings.proximity);
        Vectors2D circlePotion = camera.convertToScreen(epicentre);
        double proximityRadius = camera.scaleToScreenXValue(proximity);
        gi.draw(new Ellipse2D.Double(circlePotion.x - proximityRadius, circlePotion.y - proximityRadius, 2 * proximityRadius, 2 * proximityRadius));

        updateLinesToBody();
        for (Vectors2D p : linesToBodies) {
            gi.setColor(paintSettings.linesToObjects);
            Vectors2D worldCoord = camera.convertToScreen(p);
            gi.draw(new Line2D.Double(circlePotion.x, circlePotion.y, worldCoord.x, worldCoord.y));

            double lineToRadius = camera.scaleToScreenXValue(paintSettings.CIRCLE_RADIUS);
            gi.fill(new Ellipse2D.Double(worldCoord.x - lineToRadius, worldCoord.y - lineToRadius, 2 * lineToRadius, 2 * lineToRadius));
        }
    }

    @Override
    public void applyBlastImpulse(double blastPower) {
        for (Body b : bodiesEffected) {
            Vectors2D blastDir = b.position.subtract(epicentre);
            double distance = blastDir.length();
            if (distance == 0) return;

            //Not physically correct as it should be blast * radius to object ^ 2 as the pressure of an explosion in 2D dissipates
            double invDistance = 1 / distance;
            double impulseMag = blastPower * invDistance;
            b.applyLinearImpulseToCentre(blastDir.normalize().scalar(impulseMag));
        }
    }
}