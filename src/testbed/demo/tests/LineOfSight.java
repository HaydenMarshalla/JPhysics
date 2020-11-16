package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.ShadowCasting;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class LineOfSight {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, -40), 3.0);

        {
            Body top = new Body(new Polygon(900.0, 20.0), -20, 500);
            top.setDensity(0);
            temp.addBody(top);

            Body right = new Body(new Polygon(500.0, 20.0), 900, 20);
            right.setOrientation(1.5708);
            right.setDensity(0);
            temp.addBody(right);

            Body bottom = new Body(new Polygon(900.0, 20.0), 20, -500);
            bottom.setDensity(0);
            temp.addBody(bottom);

            Body left = new Body(new Polygon(500.0, 20.0), -900, -20);
            left.setOrientation(1.5708);
            left.setDensity(0);
            temp.addBody(left);
        }

        {
            Body b = new Body(new Circle(60.0), -200, -60);
            b.setDensity(0);
            temp.addBody(b);

            Body left = new Body(new Polygon(20.0, 20.0), 0, 200);
            left.setDensity(0);
            temp.addBody(left);
        }

        ShadowCasting b = new ShadowCasting(new Vectors2D(), 1100);
        testBedWindow.add(b);
    }
}
