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
        Vectors2D min = camera.scaleToScreen(body.aabb.getMin().addi(body.position));
        Vectors2D max = camera.scaleToScreen(body.aabb.getMax().addi(body.position));

        polyBB.moveTo(min.x, min.y);
        polyBB.lineTo(min.x, max.y);
        polyBB.lineTo(max.x, max.y);
        polyBB.lineTo(max.x, min.y);

        polyBB.closePath();
        g.draw(polyBB);
    }

    public void drawCOMS(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.centreOfMass);
        Vectors2D circlePotion = camera.scaleToScreen(body.position);
        Vectors2D line = new Vectors2D(5, 0);
        orient.mul(line);
        line = line.scalar(camera.scaleToScreenXValue(1));
        Line2D lin = new Line2D.Double(circlePotion.x - line.x, circlePotion.y - line.y, circlePotion.x + line.x, circlePotion.y + line.y);
        g.draw(lin);
        Line2D lin2 = new Line2D.Double(circlePotion.x - line.y, circlePotion.y + line.x, circlePotion.x + line.y, circlePotion.y - line.x);
        g.draw(lin2);
    }
}
