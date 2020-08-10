package library;

import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Path2D;

public class Polygon extends Shapes {
    public Vectors2D[] vertices;
    public Vectors2D[] normals;

    public Polygon(Vectors2D[] vertList) {
        this.vertices = vertList;
        calcNormals();
    }

    public Polygon(double width, double height) {
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

    public Polygon(int radius, int noOfSides) {
        for (int i = 0; i < noOfSides; i++) {
            double angle = 2 * Math.PI / noOfSides * (i + 0.75);
            double pointX = body.position.x + radius * StrictMath.cos(angle);
            double pointY = body.position.y + radius * StrictMath.sin(angle);
            vertices[i] = new Vectors2D(pointX, pointY);
        }
    }

    public void calcNormals() {
        normals = new Vectors2D[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            Vectors2D face = vertices[i + 1 == vertices.length ? 0 : i + 1].subtract(vertices[i]);
            normals[i] = face.normal().normalize().negative();
        }
    }

    @Override
    public void calcMass(double density) {
        Vectors2D centroidDistVec = new Vectors2D(0.0, 0.0);
        double area = 0.0;
        double I = 0.0;
        double k = 1.0 / 3.0;

        for (int i = 0; i < vertices.length; ++i) {
            Vectors2D point1 = vertices[i];
            Vectors2D point2 = vertices[(i + 1) % vertices.length];
            double areaOfParallelogram = point1.crossProduct(point2);
            double triangleArea = 0.5 * areaOfParallelogram;
            area += triangleArea;

            double weight = triangleArea * k;
            centroidDistVec.addi(point1.scalar(weight));
            centroidDistVec.addi(point2.scalar(weight));

            double intx2 = point1.x * point1.x + point2.x * point1.x + point2.x * point2.x;
            double inty2 = point1.y * point1.y + point2.y * point1.y + point2.y * point2.y;
            I += (0.25 * k * areaOfParallelogram) * (intx2 + inty2);
        }
        centroidDistVec = centroidDistVec.scalar(1.0 / area);

        for (int i = 0; i < vertices.length; ++i) {
            vertices[i] = vertices[i].subtract(centroidDistVec);
        }

        body.mass = density * area;
        body.invMass = (body.mass != 0.0) ? 1.0 / body.mass : 0.0;
        body.I = I * density;
        body.invI = (body.I != 0.0) ? 1.0 / body.I : 0.0;
    }

    @Override
    public void createAABB() {
        Vectors2D firstPoint = orient.mul(vertices[0], new Vectors2D());
        double minX = firstPoint.x;
        double maxX = firstPoint.x;
        double minY = firstPoint.y;
        double maxY = firstPoint.y;

        for (int i = 1; i < vertices.length; i++) {
            Vectors2D point = orient.mul(vertices[i], new Vectors2D());
            double px = point.x;
            double py = point.y;

            if (px < minX) {
                minX = px;
            } else if (px > maxX) {
                maxX = px;
            }

            if (py < minY) {
                minY = py;
            } else if (py > maxY) {
                maxY = py;
            }
        }
        body.aabb = new AABB(new Vectors2D(minX, minY).add(body.position), new Vectors2D(maxX, maxY).add(body.position));

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Path2D.Double s = new Path2D.Double();
        for (int i = 0; i < vertices.length; i++) {
            Vectors2D v = new Vectors2D(this.vertices[i]);
            orient.mul(v);
            v.add(body.position);
            if (i == 0) {
                s.moveTo(v.x, v.y);
            } else {
                s.lineTo(v.x,v.y);
            }
        }
        s.closePath();
        if (body.mass == 0.0) {
            g2.setColor(new Color(32, 57, 32));
            g2.fill(s);
            g2.setColor(new Color(127, 229, 127));
        } else {
            g2.setColor(new Color(55, 45, 46));
            g2.fill(s);
            g2.setColor(new Color(231, 178, 177));
        }
        g2.draw(s);
    }

    @Override
    public Type getType() {
        return Type.ePolygon;
    }
}