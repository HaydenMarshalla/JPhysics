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

        //Floor
        {
            Vectors2D[] b1Verts = {new Vectors2D( 50,10), new Vectors2D(30,10), new Vectors2D(50,0)};
            Body b1 = new Body(new Polygon(b1Verts), 0, 0);
            b1.setDensity(0);
            temp.addBody(b1);
        }

        {
            ParticleExplosion p = new ParticleExplosion(new Vectors2D(), temp, 100);
            p.createParticles(0.5, 100000, 20);
            p.applyBlastImpulse(100);
            testBedWindow.add(p);
        }
    }
}