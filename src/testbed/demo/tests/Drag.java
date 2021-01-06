package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Drag {
    public static final String[] text = {"Drag:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        for (int i = 0; i < 13; i++) {
            Body b1 = new Body(new Circle(10.0), -190 + (30 * i), 100);
            b1.linearDampening = 1.0 * i;
            temp.addBody(b1);
            b1.restitution = 0;
        }

        Body b4 = new Body(new Polygon(200.0, 10.0), 0, -100);
        b4.setDensity(0);
        b4.restitution = 1;
        temp.addBody(b4);
    }
}
