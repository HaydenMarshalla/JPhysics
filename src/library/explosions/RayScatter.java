package library.explosions;

import library.dynamics.Body;
import library.dynamics.Ray;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.util.ArrayList;

public class RayScatter {
    private final Ray[] rays;
    private final int noOfRays;
    private Vectors2D epicentre;

    public Vectors2D getEpicentre() {
        return epicentre;
    }

    public RayScatter(Vectors2D epicentre, int noOfRays) {
        rays = new Ray[noOfRays];
        this.epicentre = epicentre;
        this.noOfRays = noOfRays;
    }

    //Casts rays in 360 degrees with equal spacing
    public void castRays(int distance) {
        double angle = 6.28319 / noOfRays;
        Vectors2D direction = new Vectors2D(1, 1);
        Matrix2D u = new Matrix2D(angle);
        for (int i = 0; i < rays.length; i++) {
            rays[i] = new Ray(epicentre, direction, distance);
            u.mul(direction);
        }
    }

    public void updateRays(ArrayList<Body> worldBodies) {
        for (Ray ray : rays) {
            ray.updateProjection(worldBodies);
        }
    }

    public void draw(Graphics2D g2d, ColourSettings paintSettings, Camera camera) {
        for (Ray ray : rays) {
            ray.draw(g2d, paintSettings, camera);
        }
    }

    public Ray[] getRays() {
        return rays;
    }

    public void changeEpicentre(Vectors2D v) {
        this.epicentre = v;
        for (Ray ray : rays) {
            ray.setStartPoint(epicentre);
        }
    }
}
