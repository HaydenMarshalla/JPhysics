package testbed.demo.tests;

import library.Body;
import library.Circle;
import library.Polygon;
import library.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class BouncingObject {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        {
            Body b = new Body(new Circle(20), -200, 100);
            b.velocity = new Vectors2D(10, 0);
            b.restitution = 1;
            b.addTrail(100,1);
            temp.addBody(b);
        }

        {
            Body b = new Body(new Polygon(200.0, 20.0), 0, -200);
            b.setDensity(0);
            temp.addBody(b);
        }
    }
}
