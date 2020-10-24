package library.geometry;

import library.dynamics.Body;
import testbed.Camera;
import library.math.Matrix2D;
import library.math.Vectors2D;
import library.utils.ColourSettings;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public abstract class Shapes {
    public Body body;
    public Matrix2D orient = new Matrix2D();

    Shapes() {
    }

    public abstract void calcMass(double density);

    public abstract void createAABB();

    public abstract void draw(Graphics2D g, ColourSettings paintSettings, Camera camera);

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

    public void drawCOMS(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.centreOfMass);
        Vectors2D circleCentre = body.position;
        Vectors2D line = new Vectors2D(paintSettings.COM_RADIUS, 0);
        orient.mul(line);

        Vectors2D beginningOfLine = camera.convertToScreen(circleCentre.addi(line));
        Vectors2D endOfLine = camera.convertToScreen(circleCentre.subtract(line));
        Line2D lin1 = new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y);
        g.draw(lin1);

        beginningOfLine = camera.convertToScreen(circleCentre.addi(line.normal()));
        endOfLine = camera.convertToScreen(circleCentre.subtract(line.normal()));
        Line2D lin2 = new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y);
        g.draw(lin2);
    }
}
