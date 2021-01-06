package testbed.demo.tests;

import library.explosions.ProximityExplosion;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class ProximityExplosionTest {
    public static final String[] text = {"Proximity Explosions:", "Left click: casts an explosion"};
    public static boolean active = false;
    public static ProximityExplosion p;

    public static void load(TestBedWindow testBedWindow) {
        active = true;
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        testBedWindow.setCamera(new Vectors2D(0, 300), 2.0);

        testBedWindow.buildExplosionDemo();

        p = new ProximityExplosion(new Vectors2D(), 200);
        testBedWindow.add(p);

    }
}