package library;

import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends Shapes {
    double radius;

    public Circle(double radius) {
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
        body.aabb = new AABB(new Vectors2D(body.position.x - radius, body.position.y - radius),
                new Vectors2D(body.position.x + radius, body.position.y + radius));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D gi = (Graphics2D) g;
        gi.setColor(Color.BLACK);
        gi.fill(new Ellipse2D.Double(body.position.x  - radius, body.position.y - radius, 2 * radius, 2 * radius));
        gi.setColor(Color.CYAN);
        gi.draw(new Ellipse2D.Double(body.position.x - radius, body.position.y - radius, 2 * radius, 2 * radius));
    }

    @Override
    public Type getType() {
        return Type.eCircle;
    }
}
