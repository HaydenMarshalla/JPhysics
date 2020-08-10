package library;

import library.math.Vectors2D;

import java.awt.*;

public class Polygon extends Shapes {
    public Vectors2D[] vertices;
    public Vectors2D[] normals;

    Polygon(Vectors2D[] vertList) {
        this.vertices = vertList;
        calcNormals();
    }

    Polygon(double width, double height) {
        vertices = new Vectors2D[4];
        vertices[0] = new Vectors2D(-width, -height);
        vertices[1] = new Vectors2D(width, -height);
        vertices[2] = new Vectors2D(width, height);
        vertices[3] = new Vectors2D(-width, height);
        normals = new Vectors2D[4];
        normals[0] = new Vectors2D(0.0, -1.0);
        normals[1] = new Vectors2D(1.0, 0.0);
        normals[2] = new Vectors2D(0.0, 1.0);
        normals[3] = new Vectors2D(-1.0, 0.0);
    }

    Polygon(int radius, int noOfSides) {
        for (int i = 0; i < noOfSides; i++) {
            double angle = 2 * Math.PI / noOfSides * (i + 0.75);
            double pointX = body.position.x + radius * StrictMath.cos(angle);
            double pointY = body.position.y + radius * StrictMath.sin(angle);
            vertices[i] = new Vectors2D(pointX, pointY);
        }
    }

    public void calcNormals(){
        normals = new Vectors2D[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            Vectors2D face = vertices[i + 1 == vertices.length ? 0 : i + 1].subtract(vertices[i]);
            normals[i] = face.normal().normalize().negative();
        }
    }

    @Override
    public void calcMass(double density) {

    }

    @Override
    public void createAABB() {

    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public Type getType() {
        return Type.ePolygon;
    }
}