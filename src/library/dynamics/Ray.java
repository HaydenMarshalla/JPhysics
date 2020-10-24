package library.dynamics;

import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import library.utils.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Ray {
    private Vectors2D startPoint;
    private int distance;
    private Vectors2D direction;

    public Vectors2D getDirection() {
        return direction;
    }

    public Ray(Vectors2D startPoint, Vectors2D direction, int distance) {
        this.startPoint = startPoint;
        this.direction = direction.getNormalized();
        this.distance = distance;
    }

    public Ray(double direction, int distance) {
        this(new Vectors2D(), new Vectors2D(direction), distance);
    }

    public Ray(Vectors2D direction, int distance) {
        this(new Vectors2D(), direction, distance);
    }

    public Ray(Vectors2D startPoint, double direction, int distance) {
        this(startPoint, new Vectors2D(direction), distance);
    }

    public void changeDirection(Vectors2D newDirection) {
        direction = newDirection;
    }

    private RayInformation intersectingBodiesInfo = null;

    public RayInformation getRayInformation() {
        return intersectingBodiesInfo;
    }

    public void updateProjection(ArrayList<Body> bodiesToEvaluate) {
        intersectingBodiesInfo = null;
        Vectors2D endPoint = direction.scalar(distance);
        double xOfRay = endPoint.x;
        double yOfRay = endPoint.y;

        double min_t1 = Double.POSITIVE_INFINITY;
        double min_px = 0, min_py = 0;
        boolean intersectionFound = false;
        Body closestBody = null;

        for (Body B : bodiesToEvaluate) {
            if (B.shape instanceof Polygon) {
                Polygon poly = (Polygon) B.shape;
                for (int i = 0; i < poly.vertices.length; i++) {
                    Vectors2D startOfPolyEdge = poly.vertices[i];
                    Vectors2D endOfPolyEdge = poly.vertices[i + 1 == poly.vertices.length ? 0 : i + 1];
                    startOfPolyEdge = poly.orient.mul(startOfPolyEdge, new Vectors2D()).addi(B.position);
                    endOfPolyEdge = poly.orient.mul(endOfPolyEdge, new Vectors2D()).addi(B.position);
                    double dx = endOfPolyEdge.x - startOfPolyEdge.x;
                    double dy = endOfPolyEdge.y - startOfPolyEdge.y;

                    //Check to see if the lines are not parallel
                    if ((dx - xOfRay) != 0.0 && (dy - yOfRay) != 0.0) {
                        double t2 = (xOfRay * (startOfPolyEdge.y - startPoint.y) + (yOfRay * (startPoint.x - startOfPolyEdge.x))) / (dx * yOfRay - dy * xOfRay);
                        double t1 = (startOfPolyEdge.x + dx * t2 - startPoint.x) / xOfRay;

                        if (t1 > 0 && t2 >= 0 && t2 <= 1.0) {
                            if (t1 < min_t1) {
                                min_t1 = t1;
                                min_px = startPoint.x + xOfRay * t1;
                                min_py = startPoint.y + yOfRay * t1;
                                intersectionFound = true;
                                closestBody = B;
                            }
                        }
                    }
                }
            } else if (B.shape instanceof Circle) {
                Circle circle = (Circle) B.shape;
                Vectors2D ray = endPoint.copy();
                Vectors2D circleCenter = B.position.copy();
                double r = circle.radius;
                Vectors2D difInCenters = startPoint.subtract(circleCenter);

                double a = ray.dotProduct(ray);
                double b = 2 * difInCenters.dotProduct(ray);
                double c = difInCenters.dotProduct(difInCenters) - r * r;

                double discriminant = b * b - 4 * a * c;
                if (discriminant >= 0) {
                    discriminant = Math.sqrt(discriminant);

                    double t1 = (-b - discriminant) / (2 * a);
                    if (t1 >= 0 && t1 <= 1) {
                        if (t1 < min_t1) {
                            min_t1 = t1;
                            min_px = startPoint.x + xOfRay * t1;
                            min_py = startPoint.y + yOfRay * t1;
                            intersectionFound = true;
                            closestBody = B;
                        }
                    }
                }
            }
        }
        if (intersectionFound) {
            intersectingBodiesInfo = new RayInformation(closestBody, min_px, min_py);
        }
    }

    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.projectedRay);
        Vectors2D epicenter = camera.convertToScreen(startPoint);
        Vectors2D endPoint = camera.convertToScreen(direction.scalar(distance));
        g.draw(new Line2D.Double(epicenter.x, epicenter.y, endPoint.x, endPoint.y));

        g.setColor(paintSettings.rayToBody);
        if (intersectingBodiesInfo != null) {
            Vectors2D intersection = camera.convertToScreen(intersectingBodiesInfo.getCoord());
            g.draw(new Line2D.Double(epicenter.x, epicenter.y, intersection.x, intersection.y));

            double circleRadius = camera.scaleToScreenXValue(paintSettings.rayEndPointCircleSize);
            g.fill(new Ellipse2D.Double(intersection.x - circleRadius, intersection.y - circleRadius, 2.0 * circleRadius, 2.0 * circleRadius));
        }
    }
}

class RayInformation {
    private final Body b;
    private final Vectors2D coord;

    public RayInformation(Body b, double x, double y) {
        this.b = b;
        coord = new Vectors2D(x, y);
    }

    public RayInformation(Body b, Vectors2D v) {
        this.b = b;
        coord = v.copy();
    }

    public Vectors2D getCoord() {
        return coord;
    }

    public Body getB() {
        return b;
    }
}