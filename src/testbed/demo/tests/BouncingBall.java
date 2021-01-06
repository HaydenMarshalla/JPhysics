package testbed.demo.tests;

import library.dynamics.Body;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.Trail;
import testbed.demo.TestBedWindow;

public class BouncingBall {
    public static final String[] text = {"Bouncing Balls:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 200), 1.3);

        {
            for (int i = 0; i < 10; i++) {
                Body b1 = new Body(new Circle(5.0), -81.0 + (i * 17), 410.0);
                temp.addBody(b1);
            }
        }

        {
            for (int y = 0; y < 20; y++) {
                for (int x = 0; x < 10; x++) {
                    Body b;
                    if (y % 2 == 1) {
                        b = new Body(new Circle(2.0), -10 + (5 * 20) - (x * 20), 10 + y * 20);
                    } else {
                        b = new Body(new Circle(2.0), (5 * 20) - (x * 20), 10 + y * 20);
                    }
                    b.setDensity(0);
                    temp.addBody(b);
                }
            }
        }

        Body b1 = new Body(new Polygon(5.0, 190.0), -100, 190);
        b1.setDensity(0);
        temp.addBody(b1);

        Body b2 = new Body(new Polygon(5.0, 190.0), 100, 190);
        b2.setDensity(0);
        temp.addBody(b2);

        Body b3 = new Body(new Polygon(105.0, 5.0), 0, -5);
        b3.setDensity(0);
        temp.addBody(b3);
    }
}