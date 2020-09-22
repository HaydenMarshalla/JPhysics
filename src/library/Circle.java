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
        body.aabb = new AABB(new Vectors2D(-radius, -radius), new Vectors2D(radius, radius));
    }

    @Override
    public void draw(Graphics g, ColourSettings paintSettings, Camera camera) {
        Graphics2D gi = (Graphics2D) g;
        gi.setColor(paintSettings.shapeFill);
        Vectors2D circlePotion = camera.scaleToScreen(body.position);
        double drawnRadius = camera.scaleToScreenXValue(radius);
        gi.fill(new Ellipse2D.Double(circlePotion.x - drawnRadius, circlePotion.y - drawnRadius, 2 * drawnRadius, 2 * drawnRadius));
        gi.setColor(paintSettings.shapeOutLine);
        gi.draw(new Ellipse2D.Double(circlePotion.x - drawnRadius, circlePotion.y - drawnRadius, 2 * drawnRadius, 2 * drawnRadius));
    }

    @Override
    public Type getType() {
        return Type.eCircle;
    }
}
