package testbed.demo.tests;

import library.dynamics.World;
import library.explosions.RaycastExplosion;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class RaycastExplosionTest {
    public static final String[] text = {"Raycast Explosions:", "Left click: casts an explosion"};
    public static boolean active = false;
    public static RaycastExplosion r;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        testBedWindow.setCamera(new Vectors2D(0, 300), 2.0);
        World temp = testBedWindow.getWorld();
        active = true;

        testBedWindow.buildExplosionDemo();

        r = new RaycastExplosion(new Vectors2D(0, 1), 100, 1000, temp.bodies);
        testBedWindow.add(r);

    }
}
