package library.geometry;

import testbed.Camera;
import library.collision.AABB;
import library.math.Vectors2D;
import library.utils.ColourSettings;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends Shapes {
    public double radius;

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
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.shapeFill);
        Vectors2D circlePotion = camera.scaleToScreen(body.position);
        double drawnRadius = camera.scaleToScreenXValue(radius);
        g.fill(new Ellipse2D.Double(circlePotion.x - drawnRadius, circlePotion.y - drawnRadius, 2 * drawnRadius, 2 * drawnRadius));
        g.setColor(paintSettings.shapeOutLine);
        g.draw(new Ellipse2D.Double(circlePotion.x - drawnRadius, circlePotion.y - drawnRadius, 2 * drawnRadius, 2 * drawnRadius));
    }
}
