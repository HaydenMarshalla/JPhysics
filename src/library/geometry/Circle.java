package library.geometry;

import testbed.Camera;
import library.collision.AABB;
import library.math.Vectors2D;
import testbed.ColourSettings;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Circle class to create a circle object.
 */
public class Circle extends Shapes {
    public double radius;

    /**
     * Constructor for a circle.
     *
     * @param radius Desired radius of the circle.
     */
    public Circle(double radius) {
        this.radius = radius;
    }

    /**
     * Calculates the mass of a circle.
     *
     * @param density The desired density to factor into the calculation.
     */
    @Override
    public void calcMass(double density) {
        body.mass = StrictMath.PI * radius * radius * density;
        body.invMass = (body.mass != 0.0f) ? 1.0f / body.mass : 0.0f;
        body.I = body.mass * radius * radius;
        body.invI = (body.I != 0.0f) ? 1.0f / body.I : 0.0f;
    }

    /**
     * Generates an AABB and binds it to the body.
     */
    @Override
    public void createAABB() {
        body.aabb = new AABB(new Vectors2D(-radius, -radius), new Vectors2D(radius, radius));
    }

    /**
     * Debug draw method for a circle.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    @Override
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        if (body.mass == 0.0) {
            g.setColor(paintSettings.staticFill);
        } else {
            g.setColor(paintSettings.shapeFill);
        }
        Vectors2D circlePotion = camera.convertToScreen(body.position);
        double drawnRadius = camera.scaleToScreenXValue(radius);
        g.fill(new Ellipse2D.Double(circlePotion.x - drawnRadius, circlePotion.y - drawnRadius, 2 * drawnRadius, 2 * drawnRadius));
        if (body.mass == 0.0) {
            g.setColor(paintSettings.staticOutLine);
        } else {
            g.setColor(paintSettings.shapeOutLine);
        }
        g.draw(new Ellipse2D.Double(circlePotion.x - drawnRadius, circlePotion.y - drawnRadius, 2 * drawnRadius, 2 * drawnRadius));
    }
}
