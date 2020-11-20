package testbed.demo.tests;

import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class ParticleExplosionTest {
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        testBedWindow.setCamera(new Vectors2D(0, 300), 2.0);
        active = true;

        testBedWindow.buildExplosionDemo();
    }
}