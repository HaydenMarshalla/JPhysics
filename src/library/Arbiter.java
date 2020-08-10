package library;

import library.math.Vectors2D;

public class Arbiter {
    Body A;
    Body B;

    Arbiter(Body a, Body b) {
        this.A = a;
        this.B = b;
    }

    public void broadPhase() {

    }

    private double penetration;
    public Vectors2D normal = new Vectors2D();
    public final Vectors2D[] contacts = new Vectors2D[2];
    public int contactCount = 0;

    public void narrowPhase() {

    }

    public double restitution;

    public void applyImpulse() {

    }

    public void penetrationResolution() {

    }
}