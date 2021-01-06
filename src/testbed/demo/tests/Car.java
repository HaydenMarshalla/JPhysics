package testbed.demo.tests;

import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Car {
    public static final String[] text = {"Car:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, 0)));
        World world = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 0), 1.4);

    }
}
