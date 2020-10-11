package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.demo.TestBedWindow;

import java.util.Random;

public class MixedShapes {
    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(-0, 200), 2.2);

        //Polygon containers
        {
            Camera camera = testBedWindow.getCamera();

            Body b1 = new Body(new Polygon(15.0, 100.0), -300, 0);
            b1.setDensity(0);
            temp.addBody(b1);

            Body b2 = new Body(new Polygon(15.0, 100.0), 300, 0);
            b2.setDensity(0);
            temp.addBody(b2);

            Body b3 = new Body(new Polygon(315.0, 15.0), 0, -115);
            b3.setDensity(0);
            temp.addBody(b3);
        }

        //Circle obstacles
        {
            for (int i = 0; i < 3; i++){
                Body b1 = new Body(new Circle(20.0), -200 + (200*i), 150);
                b1.setDensity(0);
                temp.addBody(b1);
            }

            for (int i = 0; i < 2; i++){
                Body b1 = new Body(new Circle(20.0), -100 + (200*i), 300);
                b1.setDensity(0);
                temp.addBody(b1);
            }
        }

        {
            Random r = new Random();
            for (int i = 0; i < 20; i++) {
                Body b = temp.addBody(new Body(new Circle(r.nextInt(30)+10), r.nextInt(400)-200,r.nextInt(300)+500));
                b.restitution = 0.8;
                temp.addBody(b);
            }

            for (int i = 0; i < 20; i++) {
                Body b = temp.addBody(new Body(new Polygon(r.nextInt(40)+10,r.nextInt(7)+3), r.nextInt(400)-200,r.nextInt(300)+500));
                b.restitution = 0.8;
                temp.addBody(b);
            }
        }
    }
}
