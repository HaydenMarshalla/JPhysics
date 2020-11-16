package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.Ray;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Raycast {
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, -40), 2.75);
        active = true;

        {
            Body top = new Body(new Polygon(900.0, 20.0), -20, 500);
            top.setDensity(0);
            temp.addBody(top);

            Body right = new Body(new Polygon(500.0, 20.0), 900, 20);
            right.setOrientation(1.5708);
            right.setDensity(0);
            temp.addBody(right);

            Body bottom = new Body(new Polygon(900.0, 20.0), 20, -500);
            bottom.setDensity(0);
            temp.addBody(bottom);

            Body left = new Body(new Polygon(500.0, 20.0), -900, -20);
            left.setOrientation(1.5708);
            left.setDensity(0);
            temp.addBody(left);
        }

        {
            testBedWindow.generateRandomObjects(new Vectors2D(-880, -480), new Vectors2D(880, 480), 30, 100);
            testBedWindow.setStaticWorldBodies();
        }

        {
            Ray r = new Ray(new Vectors2D(), new Vectors2D(0, 1), 1000);
            testBedWindow.add(r);
        }
    }

    public static void action(Ray r) {
        Matrix2D u = new Matrix2D();
        u.set(-0.0006);
        u.mul(r.getDirection());
    }
}
