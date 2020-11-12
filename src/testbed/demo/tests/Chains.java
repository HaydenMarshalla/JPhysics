package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.joints.Joint;
import library.joints.JointToBody;
import library.joints.JointToPoint;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Chains {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
       /* Body b = new Body(new Circle(60.0), 0, 0);
        b.setDensity(0);
        temp.addBody(b);*/

        Body b2 = new Body(new Polygon(5.0, 10.0), 0, -60);
        temp.addBody(b2);

        Joint j1 = new JointToPoint(temp.bodies.get(0), new Vectors2D(), 40, 20, 10, true, new Vectors2D(5, 0));
        temp.addJoint(j1);

    /*    Body[] bodyList = new Body[20];
        int maxChainLength = 20;
        for (int i = 0; i < 20; i++) {
            Body b2 = new Body(new Polygon(20.0, 5.0), -20+40.0 * maxChainLength / 2 - (40 * i), 200);
            temp.addBody(b2);
            bodyList[i] = b2;
        }

        for (int i = 1; i < 20; i++) {
            Joint j1 = new JointToBody(bodyList[i - 1], bodyList[i], 20, 20, 10, true, new Vectors2D(-20, 0), new Vectors2D(20, 0));
            temp.addJoint(j1);
        }*/
    }
}