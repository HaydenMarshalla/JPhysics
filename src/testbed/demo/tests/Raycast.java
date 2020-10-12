package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.Ray;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Raycast {

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        {
            Body b1 = new Body(new Polygon(20.0, 20.0), 0, -100);
            b1.setDensity(0);
            temp.addBody(b1);

            Body b2 = new Body(new Circle(20), 0, 100);
            b2.setDensity(0);
            temp.addBody(b2);

            Body b3 = new Body(new Polygon(20.0, 20.0), 46, -140);
            b3.setDensity(0);
            temp.addBody(b3);

            Body b4 = new Body(new Circle(20), 230, 400);
            b4.setDensity(0);
            temp.addBody(b4);
        }

        {
            Ray r = new Ray(new Vectors2D(0, 1), 1000);
            testBedWindow.add(r);
        }
    }

    public static void action(Ray r) {
        Matrix2D u = new Matrix2D();
        u.set(-0.0000001);
        u.mul(r.getDirection());
    }
}
