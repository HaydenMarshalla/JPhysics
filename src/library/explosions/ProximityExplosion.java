package library.explosions;

import library.dynamics.Body;
import library.math.Vectors2D;
import testbed.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Models proximity explosions.
 */
public class ProximityExplosion implements Explosion {
    private final int proximity;
    private Vectors2D epicentre;

    /**
     * Constructor.
     *
     * @param epicentre The epicentre of the explosion.
     * @param radius    The proximity in which bodies are effected.
     */
    public ProximityExplosion(Vectors2D epicentre, int radius) {
        this.epicentre = epicentre;
        proximity = radius;
    }

    /**
     * Sets the epicentre to a different coordinate.
     *
     * @param v The vector position of the new epicentre.
     */
    @Override
    public void setEpicentre(Vectors2D v) {
        epicentre = v;
    }

    public ArrayList<Body> bodiesEffected = new ArrayList<>();

    /**
     * Updates the arraylist to reevaluate what bodies are effected/within the proximity.
     *
     * @param bodiesToEvaluate Arraylist of bodies in the world to check.
     */
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

    /**
     * Updates the lines to body array for the debug drawer.
     */
    public void updateLinesToBody() {
        linesToBodies.clear();
        for (Body b : bodiesEffected) {
            linesToBodies.add(b.position);
        }
    }

    /**
     * Debug draw method for proximity and effected objects.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    @Override
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.proximity);
        Vectors2D circlePotion = camera.convertToScreen(epicentre);
        double proximityRadius = camera.scaleToScreenXValue(proximity);
        g.draw(new Ellipse2D.Double(circlePotion.x - proximityRadius, circlePotion.y - proximityRadius, 2 * proximityRadius, 2 * proximityRadius));

        updateLinesToBody();
        for (Vectors2D p : linesToBodies) {
            g.setColor(paintSettings.linesToObjects);
            Vectors2D worldCoord = camera.convertToScreen(p);
            g.draw(new Line2D.Double(circlePotion.x, circlePotion.y, worldCoord.x, worldCoord.y));

            double lineToRadius = camera.scaleToScreenXValue(paintSettings.CIRCLE_RADIUS);
            g.fill(new Ellipse2D.Double(worldCoord.x - lineToRadius, worldCoord.y - lineToRadius, 2 * lineToRadius, 2 * lineToRadius));
        }
    }

    /**
     * Applies blast impulse to all effected bodies centre of mass.
     *
     * @param blastPower Blast magnitude.
     */
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