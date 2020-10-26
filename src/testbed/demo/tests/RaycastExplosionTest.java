package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.explosions.Explosion;
import library.explosions.RaycastExplosion;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class RaycastExplosionTest {
    public static boolean active = false;
    public static Explosion r;
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        active = true;

        {
            Body b = new Body(new Polygon(20.0, 100.0), -40, 100);
            b.setDensity(1);
            b.velocity = new Vectors2D(10,0);
            temp.addBody(b);

            Body b1 = new Body(new Polygon(200.0, 20.0), 0, -100);
            b1.setDensity(0);
            temp.addBody(b1);
        }

        {
            Body b = new Body(new Circle(20), 100, 100);
            b.setDensity(0);
            temp.addBody(b);
        }

        {
            r = new RaycastExplosion(new Vectors2D(), 10, 1000, temp.bodies);
            testBedWindow.add(r);
        }
    }
}
