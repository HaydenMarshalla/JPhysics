package library;

import library.math.Vectors2D;

public class Body {
    Vectors2D position;
    Vectors2D velocity;
    Vectors2D force;

    double angularVelocity;
    double torque;

    double restitution;
    double mass,invMass,I, invI;

    double orientation;

    Shapes shape;

    Body(Shapes shape, int x, int y){
        this.shape = shape;
        this.shape.body = this;

        position = new Vectors2D(x,y);
        velocity = new Vectors2D(0,0);
        force = new Vectors2D(0,0);

        angularVelocity = 0;
        torque = 0;

        restitution = 0.2;

        this.shape.calcMass(1.0);
        this.shape.createAABB();

        orientation = 0;
    }
}
