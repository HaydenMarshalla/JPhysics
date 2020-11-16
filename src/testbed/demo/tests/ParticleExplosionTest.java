package testbed.demo.tests;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Polygon;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class ParticleExplosionTest {
    public static boolean active = false;

    public static void load(TestBedWindow testBedWindow) {
        testBedWindow.setWorld(new World(new Vectors2D(0, -9.81)));
        World temp = testBedWindow.getWorld();
        testBedWindow.setCamera(new Vectors2D(0, 300), 2.0);
        active = true;

        buildExplosionDemo(temp);
    }

    public static void buildExplosionDemo(World temp) {
        {
            buildShelf(temp, 50.0, 300.0);
            buildShelf(temp, 450.0, 400.0);
        }

        Body floor = new Body(new Polygon(20000.0, 2000.0), 0, -2000);
        floor.setDensity(0);
        temp.addBody(floor);

        Body reflect = new Body(new Polygon(40.0, 5.0), -100, 330);
        reflect.setOrientation(0.785398);
        reflect.setDensity(0);
        temp.addBody(reflect);

        {
            Body top = new Body(new Polygon(120.0, 10.0), 450, 210);
            top.setDensity(0);
            temp.addBody(top);

            Body side1 = new Body(new Polygon(100.0, 10.0), 340, 100);
            side1.setOrientation(1.5708);
            side1.setDensity(0);
            temp.addBody(side1);

            Body side2 = new Body(new Polygon(100.0, 10.0), 560, 100);
            side2.setOrientation(1.5708);
            side2.setDensity(0);
            temp.addBody(side2);
        }
    }

    public static void buildShelf(World temp, double x, double y) {
        Body shelf = new Body(new Polygon(100.0, 10.0), x, y);
        shelf.setDensity(0);
        temp.addBody(shelf);

        int boxes = 4;
        for (int i = 0; i < boxes; i++) {
            Body box = new Body(new Polygon(10.0, 20.0), x, y + 30 + (i * 40));
            temp.addBody(box);
        }
    }
}