package library.rays;

import library.dynamics.Body;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Ray class to define and project rays in a world.
 */
public class Ray {
    private Vectors2D startPoint;
    private int distance;
    private Vectors2D direction;

    /**
     * Sets the origin of the rays projection.
     *
     * @param v Vectords2D positoin in world space.
     */
    public void setStartPoint(Vectors2D v) {
        this.startPoint = v;
    }

    /**
     * Gets the direction of the ray in radians.
     *
     * @return direction variable of type Vectors2D.
     */
    public Vectors2D getDirection() {
        return direction;
    }

    /**
     * Constructor for a ray.
     *
     * @param startPoint The origin of the rays projection.
     * @param direction  The direction of the ray points in radians.
     * @param distance   The distance the ray is projected
     */
    public Ray(Vectors2D startPoint, Vectors2D direction, int distance) {
        this.startPoint = startPoint;
        this.direction = direction.getNormalized();
        this.distance = distance;
    }

    /**
     * Convenience constructor with ray set at origin. Similar to
     * {@link #Ray(Vectors2D, Vectors2D, int)}
     *
     * @param direction The direction of the ray points in radians.
     * @param distance  The distance the ray is projected
     */
    public Ray(double direction, int distance) {
        this(new Vectors2D(), new Vectors2D(direction), distance);
    }

    /**
     * Convenience constructor with ray set at origin. Similar to
     * {@link #Ray(Vectors2D, Vectors2D, int)}
     *
     * @param direction The direction of the ray points.
     * @param distance  The distance the ray is projected
     */
    public Ray(Vectors2D direction, int distance) {
        this(new Vectors2D(), direction, distance);
    }

    /**
     * Convenience constructor. Similar to
     * {@link #Ray(Vectors2D, Vectors2D, int)}
     *
     * @param startPoint The origin of the rays projection.
     * @param direction  The direction of the ray points in radians.
     * @param distance   The distance the ray is projected
     */
    public Ray(Vectors2D startPoint, double direction, int distance) {
        this(startPoint, new Vectors2D(direction), distance);
    }

    /**
     * Sets the direction of the ray to a different value.
     *
     * @param newDirection Desired direction value
     */
    public void setDirection(Vectors2D newDirection) {
        direction = newDirection;
    }

    private RayInformation intersectingBodiesInfo = null;

    public RayInformation getRayInformation() {
        return intersectingBodiesInfo;
    }

    /**
     * Updates the projection in world space and acquires information about the closest intersecting object with the ray projection.
     *
     * @param bodiesToEvaluate Arraylist of bodies to check if they intersect with the ray projection.
     */
    public void updateProjection(ArrayList<Body> bodiesToEvaluate) {
        intersectingBodiesInfo = null;
        Vectors2D endPoint = direction.scalar(distance);
        double end_x = endPoint.x;
        double end_y = endPoint.y;

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
                    if ((dx - end_x) != 0.0 && (dy - end_y) != 0.0) {
                        double t2 = (end_x * (startOfPolyEdge.y - startPoint.y) + (end_y * (startPoint.x - startOfPolyEdge.x))) / (dx * end_y - dy * end_x);
                        double t1 = (startOfPolyEdge.x + dx * t2 - startPoint.x) / end_x;

                        if (t1 > 0 && t2 >= 0 && t2 <= 1.0) {
                            Vectors2D point = new Vectors2D(startPoint.x + end_x * t1, startPoint.y + end_y * t1);
                            double dist = point.subtract(startPoint).length();
                            if (t1 < min_t1 && dist < distance) {
                                min_t1 = t1;
                                min_px = point.x;
                                min_py = point.y;
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
                            min_px = startPoint.x + end_x * t1;
                            min_py = startPoint.y + end_y * t1;
                            intersectionFound = true;
                            closestBody = B;
                        }
                    }
                }
            }
        }
        if (intersectionFound) {
            intersectingBodiesInfo = new RayInformation(closestBody, min_px, min_py, -1);
        }
    }

    /**
     * Debug draw method for ray projection.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.projectedRay);
        Vectors2D epicenter = camera.convertToScreen(startPoint);
        Vectors2D endPoint = camera.convertToScreen(direction.scalar(distance).addi(startPoint));
        g.draw(new Line2D.Double(epicenter.x, epicenter.y, endPoint.x, endPoint.y));

        g.setColor(paintSettings.rayToBody);
        if (intersectingBodiesInfo != null) {
            Vectors2D intersection = camera.convertToScreen(intersectingBodiesInfo.getCoord());
            g.draw(new Line2D.Double(epicenter.x, epicenter.y, intersection.x, intersection.y));

            double circleRadius = camera.scaleToScreenXValue(paintSettings.RAY_DOT);
            g.fill(new Ellipse2D.Double(intersection.x - circleRadius, intersection.y - circleRadius, 2.0 * circleRadius, 2.0 * circleRadius));
        }
    }
}

