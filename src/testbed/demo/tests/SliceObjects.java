package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class SliceObjects {
    public static final String[] text = {"Slice Objects:", "Left click: Click two points on the demo window to slice objects"};
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        active = true;
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 0), 1);

        {
            Body b = new Body(new Polygon(2000.0, 50.0), 1.0f, 0.0f);
            b.setDensity(0);
            temp.addBody(b);
        }
    }
}