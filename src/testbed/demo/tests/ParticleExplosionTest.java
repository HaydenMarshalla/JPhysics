package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.explosions.ParticleExplosion;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class ParticleExplosionTest {
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        active = true;

        {
            Body top = new Body(new Polygon(900.0, 20.0), -20, 500);
            top.setDensity(0);
            temp.addBody(top);

            Body right = new Body(new Polygon(500.0, 20.0), 900, 20);
            right.setOrientation(1.5708);
            right.setDensity(0);
            temp.addBody(right);

            Body bottom = new Body(new Polygon(900.0, 20.0), 20, -500);
            bottom.setDensity(0);
            temp.addBody(bottom);

            Body left = new Body(new Polygon(500.0, 20.0), -900, -20);
            left.setOrientation(1.5708);
            left.setDensity(0);
            temp.addBody(left);
        }

        {
            ParticleExplosion p = new ParticleExplosion(new Vectors2D(), temp, 100, 1);
            p.createParticles(10, 100000, 20);
            p.applyBlastImpulse(100);
            testBedWindow.add(p, 1);
        }
    }
}