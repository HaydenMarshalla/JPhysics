package testbed.demo.tests;

import library.Body;
import library.Polygon;
import library.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Friction {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0,0), 1.4);

        //Three squares fall onto a a static platform
        {
            Body b = temp.addBody(new Body(new Polygon(200.0, 10.0), 100, -100));
            b.setOrientation(0.2);
            b.setDensity(0);

            Body b1 = temp.addBody(new Body(new Polygon(20.0, 20.0), 100 ,50));
            b1.setDensity(1);
            b1.restitution=0.2;
        }
    }
}
