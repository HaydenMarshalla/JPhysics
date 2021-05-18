package library.explosions;

import library.dynamics.Body;
import library.rays.Ray;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.util.ArrayList;

/**
 * Models rayscatter explosions.
 */
public class RayScatter {
    private final Ray[] rays;
    private final int noOfRays;
    private Vectors2D epicentre;

    /**
     * Getter for epicentre variable.
     *
     * @return Returns epicentre of explosion.
     */
    public Vectors2D getEpicentre() {
        return epicentre;
    }

    /**
     * Sets the epicentre to a different coordinate.
     *
     * @param v The vector position of the new epicentre.
     */
    public void setEpicentre(Vectors2D v) {
        this.epicentre = v;
        for (Ray ray : rays) {
            ray.setStartPoint(epicentre);
        }
    }

    /**
     * Constructor
     *
     * @param epicentre Epicentre of explosion.
     * @param noOfRays  Number of projected rays.
     */
    public RayScatter(Vectors2D epicentre, int noOfRays) {
        rays = new Ray[noOfRays];
        this.epicentre = epicentre;
        this.noOfRays = noOfRays;
    }

    /**
     * Casts rays in 360 degrees with equal spacing.
     *
     * @param distance Distance of projected rays.
     */
    public void castRays(int distance) {
        double angle = 6.28319 / noOfRays;
        Vectors2D direction = new Vectors2D(1, 1);
        Matrix2D u = new Matrix2D(angle);
        for (int i = 0; i < rays.length; i++) {
            rays[i] = new Ray(epicentre, direction, distance);
            u.mul(direction);
        }
    }

    /**
     * Updates all rays.
     *
     * @param worldBodies Arraylist of all bodies to update ray projections for.
     */
    public void updateRays(ArrayList<Body> worldBodies) {
        for (Ray ray : rays) {
            ray.updateProjection(worldBodies);
        }
    }

    /**
     * Debug draw method for rays and intersections.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        for (Ray ray : rays) {
            ray.draw(g, paintSettings, camera);
        }
    }

    /**
     * Getter for rays.
     *
     * @return Array of all rays part of the ray scatter.
     */
    public Ray[] getRays() {
        return rays;
    }
}
