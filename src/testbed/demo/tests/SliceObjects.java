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
        testBedWindow.setCamera(new Vectors2D(0, 100), 1.3);

        Body ground = new Body(new Polygon(10000.0, 2000.0), 0, -2040);
        ground.setDensity(0);
        temp.addBody(ground);

        testBedWindow.createTower(5, 0, -40);

        testBedWindow.scaleWorldFriction(0.4);
    }
}