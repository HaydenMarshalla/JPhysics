package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Drag {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        Body b1 = new Body(new Circle(10.0), 0, 100);
        temp.addBody(b1);
        b1.restitution = 1;

        Body b2 = new Body(new Circle(10.0), 50, 100);
        b2.linearDampening = 0.05;
        b2.restitution = 1;
        temp.addBody(b2);

        Body b3 = new Body(new Circle(10.0), -50, 100);
        b3.linearDampening = 0.1;
        b3.restitution = 1;
        temp.addBody(b3);

        Body b4 = new Body(new Polygon(100.0, 10.0), 0, -100);
        b4.setDensity(0);
        b3.restitution = 1;
        temp.addBody(b4);
    }
}
