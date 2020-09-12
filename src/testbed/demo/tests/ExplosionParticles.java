package testbed.demo.tests;

import library.Body;
import library.Polygon;
import library.World;
import library.math.Vectors2D;
import raycasts.ParticleExplosion;
import testbed.demo.TestBedWindow;

public class ExplosionParticles {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        {
            //ArrayList<ParticleExplosion> explosions = new ArrayList<>();
            ParticleExplosion explosion1 = new ParticleExplosion(new Vectors2D(0,0),temp,20);
            explosion1.createParticles(20, 3);
            explosion1.applyBlastImpulse(50000);
        }

        {
            Body b = new Body(new Polygon(20.0,20.0),0,100);
            b.setDensity(1);
            temp.addBody(b);
        }
    }
}