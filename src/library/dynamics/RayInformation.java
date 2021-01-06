package library.dynamics;

import library.math.Vectors2D;

public class RayInformation {
    private final Body b;
    private final Vectors2D coord;

    // Poly index is the first index of the line of intersection found
    private final int polyIndex;

    public RayInformation(Body b, double x, double y, int index) {
        this.b = b;
        coord = new Vectors2D(x, y);
        polyIndex = index;
    }

    public RayInformation(Body b, Vectors2D v, int index) {
        this.b = b;
        coord = v.copy();
        polyIndex = index;
    }

    public Vectors2D getCoord() {
        return coord;
    }

    public Body getB() {
        return b;
    }

    public int getIndex() {
        return polyIndex;
    }
}