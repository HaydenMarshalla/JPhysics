package testbed.demo.tests;

import library.dynamics.Body;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Friction {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World world = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 0), 1.4);

        {
            for (int i = 0; i < 3; i++) {
                Body ramp1 = world.addBody(new Body(new Polygon(200.0, 10.0), -150, 200 - (150 * i)));
                ramp1.setOrientation(-0.2);
                ramp1.setDensity(0);
            }

            for (int i = 0; i < 3; i++) {
                Body ramp1 = world.addBody(new Body(new Polygon(20.0, 20.0), -240, 250 - (150 * i)));
                ramp1.setOrientation(-0.2);
                ramp1.staticFriction = 0.5 - (i * 0.1);
                ramp1.dynamicFriction = 0.3 - (i * 0.1);
                ramp1.setDensity(1);
            }
        }
    }
}