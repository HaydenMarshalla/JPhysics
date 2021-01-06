package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.joints.Joint;
import library.joints.JointToBody;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Chains {
    public static final String[] text = {"Chains:"};

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, -50), 1.4);

        Body b = new Body(new Circle(60.0), 0, 0);
        b.setDensity(0);
        temp.addBody(b);

        int maxChainLength = 20;
        Body[] bodyList = new Body[maxChainLength];
        for (int i = 0; i < maxChainLength; i++) {
            Body b2 = new Body(new Polygon(20.0, 5.0), -20 + 40.0 * maxChainLength / 2 - (40 * i), 200);
            temp.addBody(b2);
            bodyList[i] = b2;

            if (i != 0) {
                Joint j1 = new JointToBody(bodyList[i - 1], bodyList[i], 1, 200, 10, true, new Vectors2D(-20, 0), new Vectors2D(20, 0));
                temp.addJoint(j1);
            }
        }
    }
}