package library.dynamics;

import library.collision.AABB;
import library.geometry.Shapes;
import library.math.Vectors2D;

/**
 * Class to create a body to add to a world.
 */
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
    public boolean affectedByGravity;
    public boolean particle;

    /**
     * Constructor for body.
     *
     * @param shape Shape to bind to body.
     * @param x     Position x in world space.
     * @param y     Position y in world space.
     */
    public Body(Shapes shape, double x, double y) {
        this.shape = shape;
        this.shape.body = this;

        position = new Vectors2D(x, y);
        velocity = new Vectors2D(0, 0);
        force = new Vectors2D(0, 0);

        angularVelocity = 0;
        torque = 0;

        restitution = 0.8;

        staticFriction = 0.5;
        dynamicFriction = 0.2;

        linearDampening = 0;
        angularDampening = 0;

        orientation = 0;
        shape.orient.set(orientation);

        this.shape.calcMass(1.0);
        this.shape.createAABB();

        particle = false;
        affectedByGravity = true;
    }

    /**
     * Applies force ot body.
     *
     * @param force        Force vector to apply.
     * @param contactPoint The point to apply the force to relative to the body in object space.
     */
    public void applyForce(Vectors2D force, Vectors2D contactPoint) {
        this.force.add(force);
        torque += contactPoint.crossProduct(force);
    }

    /**
     * Apply force to the center of mass.
     *
     * @param force Force vector to apply.
     */
    public void applyForceToCentre(Vectors2D force) {
        this.force.add(force);
    }

    /**
     * Applies impulse to a point relative to the body's center of mass.
     *
     * @param impulse      Magnitude of impulse vector.
     * @param contactPoint The point to apply the force to relative to the body in object space.
     */
    public void applyLinearImpulse(Vectors2D impulse, Vectors2D contactPoint) {
        velocity.add(impulse.scalar(invMass));
        angularVelocity += invI * contactPoint.crossProduct(impulse);
    }

    /**
     * Applies impulse to body's center of mass.
     *
     * @param impulse Magnitude of impulse vector.
     */
    public void applyLinearImpulseToCentre(Vectors2D impulse) {
        velocity.add(impulse.scalar(invMass));
    }

    /**
     * Sets the orientation of the body's shape associated with it and recalculates AABB.
     *
     * @param delta Angle of orientation.
     */
    public void setOrientation(double delta) {
        orientation = delta;
        shape.orient.set(orientation);
        shape.createAABB();
    }

    /**
     * Sets the density of the body's mass.
     *
     * @param density double value of desired density.
     */
    public void setDensity(double density) {
        if (density > 0.0) {
            shape.calcMass(density);
        } else {
            setStatic();
        }
    }

    /**
     * Sets all mass and inertia variables to zero. Object cannot be moved.
     */
    private void setStatic() {
        mass = 0.0;
        invMass = 0.0;
        I = 0.0;
        invI = 0.0;
    }
}
