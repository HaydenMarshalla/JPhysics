package testbed.demo.tests;

import library.dynamics.World;
import library.explosions.ParticleExplosion;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class ParticleExplosionTest {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        ParticleExplosion p = new ParticleExplosion(new Vectors2D(), temp, 10);
        p.createParticles(1, 100000);
        p.applyBlastImpulse(100);
    }
}