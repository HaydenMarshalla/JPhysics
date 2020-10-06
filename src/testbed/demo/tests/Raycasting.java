package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.Raycast;
import library.dynamics.RaycastScatter;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.dynamics.World;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Raycasting {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.createWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();

        {
            Body b = new Body(new Polygon(20.0, 20.0), 0, -100);
            b.setDensity(0);
            temp.addBody(b);
        }

        {
            Body b = new Body(new Circle(20), 0, 100);
            b.setDensity(0);
            temp.addBody(b);
        }

        {
            Raycast r = new Raycast(new Vectors2D(), new Vectors2D(10, 10), 100);
            testBedWindow.add(r);
        }

        {
          /*  RaycastScatter r = new RaycastScatter(new Vectors2D(), 100, temp.bodies);
            r.castRays(500);
            testBedWindow.add(r);*/
        }
    }
}
