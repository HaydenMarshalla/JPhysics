package testbed.demo.tests;

import library.dynamics.ShadowCasting;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class LineOfSight {
    public static boolean active = false;
    public static ShadowCasting b;
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, -40), 3.0);
        active = true;

        testBedWindow.generateBoxOfObjects();

        b = new ShadowCasting(new Vectors2D(-1000,0), 1100);
        testBedWindow.add(b);
    }
}
