package library.geometry;

import library.dynamics.Body;
import testbed.Camera;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.ColourSettings;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

/**
 * Abstract class presenting a geometric shape.
 */
public abstract class Shapes {
    public Body body;
    public Matrix2D orient;

    /**
     * Default constructor
     */
    Shapes() {
        orient = new Matrix2D();
        body = null;
    }

    /**
     * Calculates the mass of a shape.
     *
     * @param density The desired density to factor into the calculation.
     */
    public abstract void calcMass(double density);

    /**
     * Generates an AABB for the shape.
     */
    public abstract void createAABB();

    /**
     * Debug draw method for shape.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public abstract void draw(Graphics2D g, ColourSettings paintSettings, Camera camera);

    /**
     * Debug draw method for AABB.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public void drawAABB(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.aabb);
        Path2D polyBB = new Path2D.Double();
        Vectors2D min = camera.convertToScreen(body.aabb.getMin().addi(body.position));
        Vectors2D max = camera.convertToScreen(body.aabb.getMax().addi(body.position));

        polyBB.moveTo(min.x, min.y);
        polyBB.lineTo(min.x, max.y);
        polyBB.lineTo(max.x, max.y);
        polyBB.lineTo(max.x, min.y);

        polyBB.closePath();
        g.draw(polyBB);
    }

    /**
     * Debug draw method for center of mass.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public void drawCOMS(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.centreOfMass);
        Vectors2D centre = body.position;
        Vectors2D line = new Vectors2D(paintSettings.COM_RADIUS, 0);
        orient.mul(line);

        Vectors2D beginningOfLine = camera.convertToScreen(centre.addi(line));
        Vectors2D endOfLine = camera.convertToScreen(centre.subtract(line));
        Line2D lin1 = new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y);
        g.draw(lin1);

        beginningOfLine = camera.convertToScreen(centre.addi(line.normal()));
        endOfLine = camera.convertToScreen(centre.subtract(line.normal()));
        Line2D lin2 = new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y);
        g.draw(lin2);
    }
}
