package testbed.demo.tests;

import library.World;
import library.math.Vectors2D;
import raycasts.ParticleExplosion;
import testbed.demo.TestBedWindow;

public class ExplosionParticles {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        {
            ParticleExplosion explosion1 = new ParticleExplosion(new Vectors2D(0,0),temp,50);
            explosion1.createParticles();
        }
    }
}
