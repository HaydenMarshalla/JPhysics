package testbed.demo.tests;

import library.dynamics.Body;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Restitution {
    public static final String[] text = {"Restitution:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        //Three squares fall onto a a static platform
        {
            Body b = temp.addBody(new Body(new Polygon(200.0, 10.0), 0, -100));
            b.setDensity(0);

            for (int i = 0; i < 3; i++) {
                Body b1 = temp.addBody(new Body(new Polygon(30.0, 30.0), -100 + (i * 100), 100));
                b1.restitution = i / 3.0;
            }
        }
    }
}