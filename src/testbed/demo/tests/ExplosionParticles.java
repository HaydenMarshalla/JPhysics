package testbed.demo.tests;

import library.World;
import library.math.Vectors2D;
import raycasts.ParticleExplosion;
import testbed.demo.TestBedWindow;

public class ExplosionParticles {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, 0)));
        World temp = testBedWindow.getWorld();
        {
            //ArrayList<ParticleExplosion> explosions = new ArrayList<>();
            ParticleExplosion explosion1 = new ParticleExplosion(new Vectors2D(0,0),temp,5);
            explosion1.createParticles();
            explosion1.applyBlastImpulse(50);
        }
    }
}
