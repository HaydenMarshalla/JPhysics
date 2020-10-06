package library.dynamics;

import library.math.Matrix2D;
import library.math.Vectors2D;
import library.utils.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.util.ArrayList;

public class RaycastScatter {
    private Raycast[] rays;
    private Vectors2D epicentre;
    private int noOfRays;
    private final ArrayList<Body> worldBodies;

    public RaycastScatter(Vectors2D epicentre, int noOfRays, ArrayList<Body> bodiesList) {
        rays = new Raycast[noOfRays];
        this.epicentre = epicentre;
        this.noOfRays = noOfRays;
        worldBodies = bodiesList;
    }

    //Casts rays in 360 degrees with equal spacing
    public void castRays(int distance) {
        double angle = 6.28319 / noOfRays;
        Vectors2D direction = new Vectors2D(0, 1);
        Matrix2D u = new Matrix2D();
        u.set(angle);
        for (int i = 0; i < rays.length; i++) {
            rays[i] = new Raycast(epicentre, direction, distance);
            rays[i].updateProjection(worldBodies);
            u.mul(direction);
        }
    }

    public void updateRays() {
        for (Raycast ray : rays) {
            ray.updateProjection(worldBodies);
        }
    }

    public void draw(Graphics2D g2d, ColourSettings paintSettings, Camera camera) {
        for (Raycast ray : rays) {
            ray.draw(g2d, paintSettings, camera);
        }
    }
}