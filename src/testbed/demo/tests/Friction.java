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
            Body ramp1 = world.addBody(new Body(new Polygon(200.0, 10.0), -150, 175));
            ramp1.setOrientation(-0.2);
            ramp1.setDensity(0);

         /*   Body ramp2 = world.addBody(new Body(new Polygon(200.0, 10.0), 50, 25));
            ramp2.setOrientation(0.2);
            ramp2.setDensity(0);

            Body ramp3 = world.addBody(new Body(new Polygon(200.0, 10.0), -150, -125));
            ramp3.setOrientation(-0.2);
            ramp3.setDensity(0);*/
        }
    }
}
