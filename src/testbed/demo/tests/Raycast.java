package testbed.demo.tests;

import library.collision.Arbiter;
import library.dynamics.Body;
import library.rays.Ray;
import library.dynamics.World;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Raycast {
    public static final String[] text = {"Raycast:"};
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        testBedWindow.setCamera(new Vectors2D(-100, -20), 3.3);
        active = true;

        boolean isValid = false;
        while (!isValid) {
            isValid = true;
            testBedWindow.generateBoxOfObjects();
            for (Body b : testBedWindow.getWorld().bodies) {
                if (Arbiter.isPointInside(b, new Vectors2D())) {
                    isValid = false;
                    testBedWindow.getWorld().clearWorld();
                    break;
                }
            }
        }

        Ray r = new Ray(new Vectors2D(), new Vectors2D(0, 1), 1000);
        testBedWindow.add(r);
    }

    public static void action(Ray r) {
        Matrix2D u = new Matrix2D();
        u.set(-0.0006);
        u.mul(r.getDirection());
    }
}
