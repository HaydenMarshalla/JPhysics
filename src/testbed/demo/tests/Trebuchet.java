package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.joints.Joint;
import library.joints.JointToBody;
import library.joints.JointToPoint;
import library.math.Vectors2D;
import library.dynamics.Settings;
import testbed.demo.TestBedWindow;

public class Trebuchet {
    public static final String[] text = {"Trebuchet", "B: break tether to payload"};
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(100, 200), 2.0);
        active = true;

        Body ground = new Body(new Polygon(10000.0, 2000.0), 0, -2040);
        ground.setDensity(0);
        temp.addBody(ground);

        Body arm = new Body(new Polygon(50.0, 2.0), 0, 0);
        arm.setOrientation(0.78);
        arm.setDensity(2);
        temp.addBody(arm);

        Joint j1 = new JointToPoint(new Vectors2D(20.469, 20.469), arm, 0, 1000, 100, true, new Vectors2D(28.947, 0));
        temp.addJoint(j1);

        Body counterWeight = new Body(new Circle(5.0), 35.355, 21);
        counterWeight.setDensity(133);
        temp.addBody(counterWeight);

        Joint j2 = new JointToBody(arm, counterWeight, 20, 7000, 10, false, new Vectors2D(50, 0), new Vectors2D(0, 0));
        temp.addJoint(j2);

        Body payload = new Body(new Circle(5.0), 43.592, -35);
        payload.dynamicFriction = 0;
        payload.staticFriction = 0;
        payload.setDensity(1);
        temp.addBody(payload);

        Joint j3 = new JointToBody(arm, payload, 79, 100, 1, true, new Vectors2D(-50, 0), new Vectors2D());
        temp.addJoint(j3);

        testBedWindow.createPyramid(10,1500,-40);

        Settings.HERTZ = 400;
    }
}