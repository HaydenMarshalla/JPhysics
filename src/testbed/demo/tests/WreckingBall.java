package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.joints.Joint;
import library.joints.JointToPoint;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class WreckingBall {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 100), 1.7);

        {
            for (int x = 0; x < 15; x++) {
                for (int y = 0; y < 15; y++) {
                    Body b = new Body(new Polygon(10.0, 10.0), 110 + (x * 20), -100 + (y * 20));
                    temp.addBody(b);
                }
            }

            Body b = new Body(new Polygon(150.0, 10.0), 250, -120);
            b.setDensity(0);
            temp.addBody(b);
        }

        {
            Body b2 = new Body(new Circle(60.0), -350, 350);
            b2.setDensity(2);
            temp.addBody(b2);

            Joint j = new JointToPoint(new Vectors2D(0, 350), b2, 350, 2000, 10, true, new Vectors2D());
            temp.addJoint(j);
        }
    }
}