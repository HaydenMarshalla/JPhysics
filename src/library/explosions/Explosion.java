package library.explosions;

import library.dynamics.Body;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.util.ArrayList;

/**
 * Interface detailing what explosions need to include.
 */
public interface Explosion {
    /**
     * Applies a blast impulse to the effected bodies.
     *
     * @param blastPower The impulse magnitude.
     */
    void applyBlastImpulse(double blastPower);

    /**
     * Debug draw method for explosion and the effected objects.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    void draw(Graphics2D g, ColourSettings paintSettings, Camera camera);

    /**
     * Updates the arraylist to reevaluate what objects are effected/within the proximity.
     *
     * @param bodiesToEvaluate Arraylist of bodies in the world to check.
     */
    void update(ArrayList<Body> bodiesToEvaluate);

    /**
     * Sets the epicentre to a different coordinate.
     *
     * @param v The vector position of the new epicentre.
     */
    void setEpicentre(Vectors2D v);
}
