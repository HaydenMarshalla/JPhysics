package library;

import library.math.Matrix2D;
import library.math.Vectors2D;

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

    enum Type {
        eCircle, ePolygon
    }

    public abstract Type getType();

    public abstract void draw(Graphics g, ColourSettings paintSettings, Camera camera);

    public void drawAABB(Graphics g, ColourSettings paintSettings, Camera camera) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(paintSettings.aabb);

        Path2D polyBB = new Path2D.Double();
        Vectors2D min = camera.scaleToScreen(body.aabb.getMin().addi(body.position));
        Vectors2D max = camera.scaleToScreen(body.aabb.getMax().addi(body.position));

        polyBB.moveTo(min.x, min.y);
        polyBB.lineTo(min.x, max.y);
        polyBB.lineTo(max.x, max.y);
        polyBB.lineTo(max.x, min.y);

        polyBB.closePath();
        g2.draw(polyBB);
    }

    public void drawCOMS(Graphics g, ColourSettings paintSettings, Camera camera) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(paintSettings.centreOfMass);
        Vectors2D circlePotion = camera.scaleToScreen(body.position);
        Vectors2D line = new Vectors2D(5, 0);
        orient.mul(line);
        line = line.scalar(camera.scaleToScreenXValue(1));
        Line2D lin = new Line2D.Double(circlePotion.x - line.x, circlePotion.y - line.y, circlePotion.x + line.x, circlePotion.y + line.y);
        g2.draw(lin);
        Line2D lin2 = new Line2D.Double(circlePotion.x - line.y, circlePotion.y + line.x, circlePotion.x + line.y, circlePotion.y - line.x);
        g2.draw(lin2);
    }
}
