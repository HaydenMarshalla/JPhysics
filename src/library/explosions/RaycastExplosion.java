package library.explosions;

import library.dynamics.Body;
import library.rays.Ray;
import library.rays.RayInformation;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.util.ArrayList;

/**
 * Models raycast explosions.
 */
public class RaycastExplosion implements Explosion {
    private final RayScatter rayScatter;

    /**
     * Constructor
     *
     * @param epicentre   The epicentre of the explosion.
     * @param noOfRays    Number of projected rays.
     * @param distance    Distance of projected rays.
     * @param worldBodies The world the rays effect and are projected in.
     */
    public RaycastExplosion(Vectors2D epicentre, int noOfRays, int distance, ArrayList<Body> worldBodies) {
        rayScatter = new RayScatter(epicentre, noOfRays);
        rayScatter.castRays(distance);
        update(worldBodies);
    }

    /**
     * Sets the epicentre to a different coordinate.
     *
     * @param v The vector position of the new epicentre.
     */
    @Override
    public void setEpicentre(Vectors2D v) {
        rayScatter.setEpicentre(v);
    }

    public ArrayList<RayInformation> raysInContact = new ArrayList<>();

    /**
     * Updates the arraylist to reevaluate what objects are effected/within the proximity.
     *
     * @param bodiesToEvaluate Arraylist of bodies in the world to check.
     */
    @Override
    public void update(ArrayList<Body> bodiesToEvaluate) {
        raysInContact.clear();
        rayScatter.updateRays(bodiesToEvaluate);
        Ray[] rayArray = rayScatter.getRays();
        for (Ray ray : rayArray) {
            RayInformation rayInfo = ray.getRayInformation();
            if (rayInfo != null) {
                raysInContact.add(rayInfo);
            }
        }
    }

    /**
     * Applies a blast impulse to the effected bodies.
     *
     * @param blastPower The impulse magnitude.
     */
    @Override
    public void applyBlastImpulse(double blastPower) {
        for (RayInformation ray : raysInContact) {
            Vectors2D blastDir = ray.getCoord().subtract(rayScatter.getEpicentre());
            double distance = blastDir.length();
            if (distance == 0) return;

            double invDistance = 1 / distance;
            Vectors2D impulseMag = blastDir.normalize().scalar(blastPower * invDistance);
            Body b = ray.getB();
            b.applyLinearImpulse(impulseMag, ray.getCoord().subtract(b.position));
        }
    }

    /**
     * Debug draw method for all rays projected.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    @Override
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        rayScatter.draw(g, paintSettings, camera);
    }
}

