package testbed.demo.tests;

import library.Body;
import library.Polygon;
import library.World;
import library.math.Vectors2D;
import raycasts.ProximityExplosion;
import testbed.demo.TestBedWindow;

public class ProximityTestExplosion {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        //Three squares fall onto a a static platform
        {
            Body b = temp.addBody(new Body(new Polygon(200.0, 10.0), 0, -100));
            b.setDensity(0);

            for (int i = 0; i < 2; i++) {
                Body b1 = temp.addBody(new Body(new Polygon(30.0, 30.0), -100, 100 + (i * 100)));
                b1.setDensity(100);
            }
            ProximityExplosion p = new ProximityExplosion(new Vectors2D(0, 0), temp, 100);
        }
    }
}