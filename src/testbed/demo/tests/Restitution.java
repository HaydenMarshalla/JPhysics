package testbed.demo.tests;

import library.Body;
import library.Polygon;
import library.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Restitution {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        {
            Body b = temp.addBody(new Body(new Polygon(200.0, 20.0), 0, 0));
            b.setDensity(0);

            Body b1 = temp.addBody(new Body(new Polygon(30.0, 30.0), -100, 150));
            b1.restitution = 0.33;

            Body b2 = temp.addBody(new Body(new Polygon(30.0, 30.0), 0, 150));
            b2.restitution = 0.66;

            Body b3 = temp.addBody(new Body(new Polygon(30.0, 30.0), 100, 150));
            b3.restitution = 1;
        }
    }
}