package testbed.demo.tests;

import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class CompoundBodies {
    public static final String[] text = {"Compound Bodies:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
    }
}