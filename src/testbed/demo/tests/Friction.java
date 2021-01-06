package testbed.demo.tests;

import library.dynamics.Body;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Friction {
    public static final String[] text = {"Friction:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World world = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 0), 1.4);

        {
            for (int i = 0; i < 3; i++) {
                Body ramp = world.addBody(new Body(new Polygon(200.0, 10.0), -200 + (200 * i), 200 - (180 * i)));
                ramp.setOrientation(-0.2);
                ramp.setDensity(0);
            }

            for (int i = 0; i < 3; i++) {
                Body box = world.addBody(new Body(new Polygon(20.0, 20.0), -290 + (200 * i), 250 - (180 * i)));
                box.setOrientation(-0.2);
                box.staticFriction = 0.5 - (i * 0.1);
                box.dynamicFriction = 0.3 - (i * 0.1);
                box.setDensity(1);
            }
        }
    }
}