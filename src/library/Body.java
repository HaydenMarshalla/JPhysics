package library;

import library.math.Vectors2D;

public class Body {
    public Vectors2D position;
    public Vectors2D velocity;
    public Vectors2D force;

    public double angularVelocity;
    public double torque;

    public double restitution;
    public double mass, invMass, I, invI;

    public double orientation;

    public Shapes shape;
    public AABB aabb;

    public Body(Shapes shape, int x, int y) {
        this.shape = shape;
        this.shape.body = this;

        position = new Vectors2D(x, y);
        velocity = new Vectors2D(0, 0);
        force = new Vectors2D(0, 0);

        angularVelocity = 0;
        torque = 0;

        restitution = 0.2;

        this.shape.calcMass(1.0);
        this.shape.createAABB();

        orientation = 0;
    }
}
