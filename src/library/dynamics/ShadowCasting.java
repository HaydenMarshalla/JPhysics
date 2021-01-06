package library.dynamics;

import library.collision.Arbiter;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ShadowCasting {
    private final int distance;
    private Vectors2D startPoint;

    public void setStartPoint(Vectors2D startPoint) {
        this.startPoint = startPoint;
    }

    public ShadowCasting(Vectors2D startPoint, int distance) {
        this.startPoint = startPoint;
        this.distance = distance;
    }

    private final ArrayList<RayAngleInformation> rayData = new ArrayList<>();

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

    public void draw(Graphics2D g2d, ColourSettings paintSettings, Camera camera) {
        for (int i = 0; i < rayData.size(); i++) {
            Ray ray1 = rayData.get(i).getRAY();
            Ray ray2 = rayData.get(i + 1 == rayData.size() ? 0 : i + 1).getRAY();
            g2d.setColor(paintSettings.shadow);

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
            g2d.fill(s);
        }
    }

    public int getNoOfRays() {
        return rayData.size();
    }
}

class RayAngleInformation {
    private final Ray RAY;
    private final double ANGLE;

    public RayAngleInformation(Ray ray, double angle) {
        this.RAY = ray;
        this.ANGLE = angle;
    }

    public Ray getRAY() {
        return RAY;
    }

    public double getANGLE() {
        return ANGLE;
    }
}