package library;

import java.awt.*;

public class Circle extends Shapes {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void calcMass(double density) {
        body.mass = StrictMath.PI * radius * radius * density;
        body.invMass = (body.mass != 0.0f) ? 1.0f / body.mass : 0.0f;
        body.I = body.mass * radius * radius;
        body.invI = (body.I != 0.0f) ? 1.0f / body.I : 0.0f;
    }

    @Override
    public void createAABB() {

    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public Type getType() {
        return Type.eCircle;
    }
}
