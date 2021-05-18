package library.rays;

import library.collision.Arbiter;
import library.dynamics.Body;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * A class for generating polygons that can mimic line of sight around objects and cast shadows.
 */
public class ShadowCasting {
    private final int distance;
    private Vectors2D startPoint;

    /**
     * Setter for start point.
     *
     * @param startPoint Returns start point.
     */
    public void setStartPoint(Vectors2D startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * Constructor
     *
     * @param startPoint Origin of projecting rays.
     * @param distance   The desired distance to project the rays.
     */
    public ShadowCasting(Vectors2D startPoint, int distance) {
        this.startPoint = startPoint;
        this.distance = distance;
    }

    private final ArrayList<RayAngleInformation> rayData = new ArrayList<>();

    /**
     * Updates the all projections in world space and acquires information about all intersecting rays.
     *
     * @param bodiesToEvaluate Arraylist of bodies to check if they intersect with the ray projection.
     */
    public void updateProjections(ArrayList<Body> bodiesToEvaluate) {
        rayData.clear();
        for (Body B : bodiesToEvaluate) {
            if (Arbiter.isPointInside(B, startPoint)) {
                rayData.clear();
                break;
            }
            if (B.shape instanceof Polygon) {
                Polygon poly1 = (Polygon) B.shape;
                for (Vectors2D v : poly1.vertices) {
                    Vectors2D direction = poly1.orient.mul(v, new Vectors2D()).addi(B.position).subtract(startPoint);
                    projectRays(direction, bodiesToEvaluate);
                }
            } else {
                Circle circle = (Circle) B.shape;
                Vectors2D d = B.position.subtract(startPoint);
                double angle = Math.asin(circle.radius / d.length());
                Matrix2D u = new Matrix2D(angle);
                projectRays(u.mul(d.normalize(), new Vectors2D()), bodiesToEvaluate);
                Matrix2D u2 = new Matrix2D(-angle);
                projectRays(u2.mul(d.normalize(), new Vectors2D()), bodiesToEvaluate);
            }
        }
        rayData.sort((lhs, rhs) -> Double.compare(rhs.getANGLE(), lhs.getANGLE()));
    }

    /**
     * Projects a ray and evaluates it against all objects supplied in world space.
     *
     * @param direction        Direction of ray to project.
     * @param bodiesToEvaluate Arraylist of bodies to check if they intersect with the ray projection.
     */
    private void projectRays(Vectors2D direction, ArrayList<Body> bodiesToEvaluate) {
        Matrix2D m = new Matrix2D(0.001);
        m.transpose().mul(direction);
        for (int i = 0; i < 3; i++) {
            Ray ray = new Ray(startPoint, direction, distance);
            ray.updateProjection(bodiesToEvaluate);
            rayData.add(new RayAngleInformation(ray, Math.atan2(direction.y, direction.x)));
            m.mul(direction);
        }
    }

    /**
     * Debug draw method for all polygons generated and rays.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        for (int i = 0; i < rayData.size(); i++) {
            Ray ray1 = rayData.get(i).getRAY();
            Ray ray2 = rayData.get(i + 1 == rayData.size() ? 0 : i + 1).getRAY();
            g.setColor(paintSettings.shadow);

            Path2D.Double s = new Path2D.Double();
            Vectors2D worldStartPoint = camera.convertToScreen(startPoint);
            s.moveTo(worldStartPoint.x, worldStartPoint.y);
            if (ray1.getRayInformation() != null) {
                Vectors2D point1 = camera.convertToScreen(ray1.getRayInformation().getCoord());
                s.lineTo(point1.x, point1.y);
            }
            if (ray2.getRayInformation() != null) {
                Vectors2D point2 = camera.convertToScreen(ray2.getRayInformation().getCoord());
                s.lineTo(point2.x, point2.y);
            }
            s.closePath();
            g.fill(s);
        }
    }

    /**
     * Getter for number of rays projected.
     *
     * @return Returns size of raydata.
     */
    public int getNoOfRays() {
        return rayData.size();
    }
}

/**
 * Ray information class to store relevant data about rays and any intersection found specific to shadow casting.
 */
class RayAngleInformation {
    private final Ray RAY;
    private final double ANGLE;

    /**
     * Constructor to store ray information.
     *
     * @param ray   Ray of intersection.
     * @param angle Angle the ray is set to.
     */
    public RayAngleInformation(Ray ray, double angle) {
        this.RAY = ray;
        this.ANGLE = angle;
    }

    /**
     * Getter for RAY.
     *
     * @return returns RAY.
     */
    public Ray getRAY() {
        return RAY;
    }

    /**
     * Getter for ANGLE.
     *
     * @return returns ANGLE.
     */
    public double getANGLE() {
        return ANGLE;
    }
}