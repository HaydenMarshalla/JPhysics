package testbed.demo.tests;

import library.dynamics.Ray;
import library.dynamics.World;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Raycast {
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        testBedWindow.setCamera(new Vectors2D(0, -40), 2.75);
        active = true;

        testBedWindow.generateBoxOfObjects();

        Ray r = new Ray(new Vectors2D(), new Vectors2D(0, 1), 1000);
        testBedWindow.add(r);
    }

    public static void action(Ray r) {
        Matrix2D u = new Matrix2D();
        u.set(-0.0006);
        u.mul(r.getDirection());
    }
}
