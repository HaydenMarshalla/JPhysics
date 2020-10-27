package testbed.demo.tests;

import library.dynamics.Body;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.Trail;
import testbed.demo.TestBedWindow;

public class BouncingBall {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(300, 100), 1.0);

        {
            Body b = new Body(new Circle(20), 0, 200);
            b.velocity = new Vectors2D(30,0);
            testBedWindow.add(new Trail(10000, 100, b));
            temp.addBody(b);
        }

        {
            Body b = new Body(new Polygon(20000.0, 2000.0), 0, -2000);
            b.setDensity(0);
            temp.addBody(b);
        }
    }
}