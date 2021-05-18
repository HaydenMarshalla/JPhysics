package library.rays;

import library.dynamics.Body;
import library.math.Vectors2D;

/**
 * Ray information class to store relevant data about rays and any intersection found.
 */
public class RayInformation {
    private final Body b;
    private final Vectors2D coord;

    // Poly index is the first index of the line of intersection found
    private final int polyIndex;

    /**
     * Constructor to store information about a ray intersection.
     *
     * @param b     Body involved with ray intersection.
     * @param x     x position of intersection.
     * @param y     y position of intersection.
     * @param index Index of shapes side that intersection intersects.
     */
    public RayInformation(Body b, double x, double y, int index) {
        this.b = b;
        coord = new Vectors2D(x, y);
        polyIndex = index;
    }

    /**
     * Convenience constructor equivalent to
     * {@link #RayInformation(Body, double, double, int)}
     *
     * @param b     Body involved with ray intersection.
     * @param v     x/y position of intersection.
     * @param index Index of shapes side that intersection intersects.
     */
    public RayInformation(Body b, Vectors2D v, int index) {
        this.b = b;
        coord = v.copy();
        polyIndex = index;
    }

    /**
     * Getter for coord variable.
     *
     * @return returns coord variable of type Vectors2D.
     */
    public Vectors2D getCoord() {
        return coord;
    }

    /**
     * Getter for body variable.
     *
     * @return returns b variable of type Body.
     */
    public Body getB() {
        return b;
    }

    /**
     * Getter for index variable.
     *
     * @return returns index variable of type int.
     */
    public int getIndex() {
        return polyIndex;
    }
}