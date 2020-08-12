package library;

import library.math.Matrix2D;
import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Path2D;

public abstract class Shapes {
    public Body body;
    public Matrix2D orient = new Matrix2D();

    Shapes(){}

    public abstract void calcMass(double density);

    public abstract void createAABB();

    enum Type {
        eCircle, ePolygon
    }

    public abstract Type getType();

    public abstract void draw(Graphics g, ColourSettings paintSettings);

    public void drawAABB(Graphics g, ColourSettings paintSettings){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(paintSettings.aabb);

        Path2D polyBB = new Path2D.Double();
        Vectors2D min = body.aabb.getMin();
        Vectors2D max = body.aabb.getMax();

        polyBB.moveTo(min.x, min.y);
        polyBB.lineTo(min.x, max.y);
        polyBB.lineTo(max.x, max.y);
        polyBB.lineTo(max.x, min.y);

        polyBB.closePath();
        g2.draw(polyBB);
    }

    public void drawCOMS(Graphics g, ColourSettings paintSettings){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(paintSettings.centreOfMass);
    };
}
