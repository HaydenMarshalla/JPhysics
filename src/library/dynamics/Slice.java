package library.dynamics;

import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Slice {
    private final Vectors2D startPoint;
    private double distance;
    private Vectors2D direction;

    public Slice(Vectors2D startPoint, Vectors2D direction, double distance) {
        this.startPoint = startPoint;
        this.direction = direction.getNormalized();
        this.distance = distance;
    }

    private final ArrayList<RayInformation> intersectingBodiesInfo = new ArrayList<>();

    public void updateProjection(ArrayList<Body> bodiesToEvaluate) {
        intersectingBodiesInfo.clear();
        Vectors2D endPoint = direction.scalar(distance);
        double end_x = endPoint.x;
        double end_y = endPoint.y;

        double min_t1 = Double.POSITIVE_INFINITY;
        double min_px = 0, min_py = 0;
        int noOfIntersections = 0;

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
                            if (dist < distance) {
                                min_t1 = t1;
                                min_px = point.x;
                                min_py = point.y;
                                intersectingBodiesInfo.add(new RayInformation(B, min_px, min_py, i));
                                noOfIntersections++;
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
                if (discriminant > 0) {
                    discriminant = Math.sqrt(discriminant);

                    double t1 = (-b - discriminant) / (2 * a);
                    if (t1 >= 0 && t1 <= 1) {
                        min_px = startPoint.x + end_x * t1;
                        min_py = startPoint.y + end_y * t1;
                        double dist = startPoint.subtract(new Vectors2D(min_px, min_py)).length();
                        intersectingBodiesInfo.add(new RayInformation(B, min_px, min_py, -1));
                    }

                    double t2 = (-b + discriminant) / (2 * a);
                    if (t2 >= 0 && t2 <= 1) {
                        min_px = startPoint.x + end_x * t2;
                        min_py = startPoint.y + end_y * t2;
                        double dist = startPoint.subtract(new Vectors2D(min_px, min_py)).length();
                        intersectingBodiesInfo.add(new RayInformation(B, min_px, min_py, -1));
                    }
                }
            }
            if (noOfIntersections == 1) {
                intersectingBodiesInfo.remove(intersectingBodiesInfo.size() - 1);
                noOfIntersections = 0;
            }
        }
    }

    public void sliceObjects(World world) {
        assert (intersectingBodiesInfo.size() % 2 == 0);
        for (int i = 0; i < intersectingBodiesInfo.size(); i += 2) {
            Body b = intersectingBodiesInfo.get(i).getB();
            if (b.shape instanceof Polygon) {
                Polygon p = (Polygon) b.shape;

                RayInformation intersection1 = intersectingBodiesInfo.get(i);
                RayInformation intersection2 = intersectingBodiesInfo.get(i + 1);

                int obj1firstIndex = intersection1.getIndex();
                int secondIndex = intersection2.getIndex();
                int obj2firstIndex = obj1firstIndex;

                int totalVerticesObj1 = (obj1firstIndex + 2) + (p.vertices.length - secondIndex);
                Vectors2D[] obj1Vertz = new Vectors2D[totalVerticesObj1];

                if (obj1firstIndex + 1 >= 0) System.arraycopy(p.vertices, 0, obj1Vertz, 0, obj1firstIndex + 1);

                obj1Vertz[++obj1firstIndex] = intersectingBodiesInfo.get(i).getCoord();
                obj1Vertz[++obj1firstIndex] = intersectingBodiesInfo.get(i + 1).getCoord();

                for (int x = secondIndex + 1; x < p.vertices.length; x++) {
                    obj1Vertz[++obj1firstIndex] = p.vertices[x];
                }

                world.addBody(new Body(new Polygon(obj1Vertz), 0, 0));

                totalVerticesObj1 = secondIndex - obj2firstIndex + 2;
                Vectors2D[] obj2Vertz = new Vectors2D[totalVerticesObj1];

                int indexToAddTo = 0;
                obj2Vertz[indexToAddTo++] = intersection1.getCoord();

                for (int x = obj2firstIndex + 1; x <= secondIndex; x++) {
                    obj2Vertz[indexToAddTo++] = p.vertices[x];
                }

                obj2Vertz[totalVerticesObj1 - 1] = intersection2.getCoord();

                world.addBody(new Body(new Polygon(obj2Vertz), 0, 0));
            } else if (b.shape instanceof Circle) {

            }
            world.removeBody(b);
        }
    }

    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.projectedRay);
        Vectors2D epicenter = camera.convertToScreen(startPoint);
        Vectors2D endPoint = camera.convertToScreen(direction.scalar(distance).addi(startPoint));
        g.draw(new Line2D.Double(epicenter.x, epicenter.y, endPoint.x, endPoint.y));

        g.setColor(paintSettings.rayToBody);
        for (int i = 0; i < intersectingBodiesInfo.size(); i++) {
            if ((i + 1) % 2 == 0) {
                Vectors2D intersection1 = camera.convertToScreen(intersectingBodiesInfo.get(i - 1).getCoord());
                Vectors2D intersection2 = camera.convertToScreen(intersectingBodiesInfo.get(i).getCoord());
                g.draw(new Line2D.Double(intersection2.x, intersection2.y, intersection1.x, intersection1.y));
            }
        }
    }

    public void setDirection(Vectors2D sliceVector) {
        direction = sliceVector.subtract(startPoint);
        distance = direction.length();
        direction.normalize();
    }
}
