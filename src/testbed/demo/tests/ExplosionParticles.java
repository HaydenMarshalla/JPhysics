package testbed.demo.tests;

import library.dynamics.World;
import library.math.Vectors2D;
import library.explosions.ParticleExplosion;
import testbed.demo.TestBedWindow;

public class ExplosionParticles {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        {
            //ArrayList<ParticleExplosion> library.explosions = new ArrayList<>();
            ParticleExplosion explosion1 = new ParticleExplosion(new Vectors2D(0,0),temp,20);
            explosion1.createParticles(20, 3, 10);
            explosion1.applyBlastImpulse(500000);
            //explosion1.applyVelocityFromEpicentre(150);
        }

        {

        }
    }
}