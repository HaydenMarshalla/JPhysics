package library.dynamics;

import library.math.Vectors2D;

public class RayInformation {
    private final Body b;
    private final Vectors2D coord;

    public RayInformation(Body b, double x, double y) {
        this.b = b;
        coord = new Vectors2D(x, y);
    }

    public RayInformation(Body b, Vectors2D v) {
        this.b = b;
        coord = v.copy();
    }

    public Vectors2D getCoord() {
        return coord;
    }

    public Body getB() {
        return b;
    }
}
