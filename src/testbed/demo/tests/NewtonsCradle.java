package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.joints.Joint;
import library.joints.JointToPoint;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class NewtonsCradle {
    public static final String[] text = {"Newtons Cradle:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(), 2);

        double radius = 40.0;
        int noOfCircles = 8;
        double spread = ((noOfCircles - 1) * 80 / 2.0);

        double minX, maxX;
        minX = -spread + 40;

        {
            for (int i = 0; i < noOfCircles; i++) {
                double x = minX + (i * 80);
                Body b = new Body(new Circle(radius), x, -100);
                b.restitution = 1;
                b.staticFriction = 0;
                b.dynamicFriction = 0;
                temp.addBody(b);

                Joint j = new JointToPoint(new Vectors2D(x, 200), b, 300, 200000, 1000, true, new Vectors2D());
                temp.addJoint(j);
            }
        }

        {
            minX -= 80;
            Body b = new Body(new Circle(radius), minX - 300, 200);
            b.restitution = 1;
            b.staticFriction = 0;
            b.dynamicFriction = 0;
            temp.addBody(b);

            Joint j = new JointToPoint(new Vectors2D(minX, 200), b, 300, 200000, 1000, true, new Vectors2D());
            temp.addJoint(j);
        }
    }
}
