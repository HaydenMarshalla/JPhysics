package library;

import library.math.Matrix2D;

import java.awt.*;

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

    public abstract void draw(Graphics g);

    public void drawAABB(Graphics g){

    }
}
