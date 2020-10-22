package library.dynamics;

import library.collision.AABB;
import library.geometry.Shapes;
import library.math.Vectors2D;

public class Body {
    public double dynamicFriction;
    public double staticFriction;
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

    public double linearDampening;
    public double angularDampening;
    public double gravityScale;

    public Body(Shapes shape, double x, double y) {
        this.shape = shape;
        this.shape.body = this;

        position = new Vectors2D(x, y);
        velocity = new Vectors2D(0, 0);
        force = new Vectors2D(0, 0);

        angularVelocity = 0;
        torque = 0;

        restitution = 0.8;

        staticFriction = 0.2;
        dynamicFriction = 0.2;

        linearDampening = 0;
        angularDampening = 0;
        gravityScale = 1;

        orientation = 0;
        shape.orient.set(orientation);

        this.shape.calcMass(1.0);
        this.shape.createAABB();
    }

    public void setOrientation(double delta) {
        orientation = delta;
        shape.orient.set(orientation);
        shape.createAABB();
    }

    public void setDensity(int density) {
        if (density > 0) {
            shape.calcMass(density);
        } else {
            setStatic();
        }
    }

    private void setStatic() {
        mass = 0;
        invMass = 0;
        I = 0;
        invI = 0;
    }
}
