package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.joints.Joint;
import library.joints.JointToPoint;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Trebuchet {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        {
         /*   Body b1 = new Body(new Polygon(10.0, 50.0), 0,0);
            b1.setDensity(5);
            temp.addBody(b1);

            Joint j1 = new JointToPoint(new Vectors2D(0,0), b1, 100, 100, 10, true, new Vectors2D(5, 0));
            temp.addJoint(j1);
*/
            Body b3 = new Body(new Circle(10.0), 1, 0);
            b3.setDensity(100);
            temp.addBody(b3);
            Joint j1 = new JointToPoint(new Vectors2D(0, 0), b3, 1, 10000, 1000, true, new Vectors2D(0, 0));
            temp.addJoint(j1);

           /* Body b2 = new Body(new Polygon(15.0, 15.0), 395, 150);
            b2.setDensity(9300);
            temp.addBody(b2);

            Body b3 = new Body(new Circle(1.0), 410, 10);
            b3.setDensity(100);
            temp.addBody(b3);*/
        }

        {
        /*    for (int x = 0; x < 15; x++) {
                for (int y = 0; y < 15; y++) {
                    Body b = new Body(new Polygon(10.0, 10.0), 510 + (x * 20), -100 + (y * 20));
                    temp.addBody(b);
                }
            }

            Body b = new Body(new Polygon(150.0, 10.0), 650, -120);
            b.setDensity(0);
            temp.addBody(b);*/
        }
    }
}