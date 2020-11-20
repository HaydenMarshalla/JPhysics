package testbed.demo.tests;

import library.dynamics.Body;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class StackedObjects {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 250), 2.8);

        {
            for (int x = 0; x < 15; x++) {
                for (int y = 0; y < 25; y++) {
                    Body b = new Body(new Polygon(10.0, 10.0), -140 + (x * 20), -100 + (y * 20));
                    temp.addBody(b);
                }
            }
            for (int x = 0; x < 15; x++) {
                for (int y = 0; y < 1; y++) {
                    Body b = new Body(new Polygon(10.0, 10.0), -140 + (x * 20), 700);
                    b.setDensity(50);
                    temp.addBody(b);
                }
            }

            Body b = new Body(new Polygon(150.0, 10.0), 0, -120);
            b.setDensity(0);
            temp.addBody(b);
        }
    }
}
