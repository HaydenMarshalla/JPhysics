package testbed.junittests;

import library.collision.AABB;
import library.dynamics.Body;
import library.geometry.Circle;
import library.math.Vectors2D;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class AABBTest {
    @Test
    public void set() {
        AABB b = new AABB();
        AABB a = new AABB(new Vectors2D(10, 10), new Vectors2D(20, 20));
        b.set(a);
        Vectors2D val = new Vectors2D(10, 10);
        assertEquals(val.x, b.getMin().x, 0.0);
        assertEquals(val.y, b.getMin().y, 0.0);
        assertEquals(new Vectors2D(20, 20).x, b.getMax().x, 0.0);
        assertEquals(new Vectors2D(20, 20).y, b.getMax().y, 0.0);

    }

    @Test
    public void getMin() {
        AABB b = new AABB();
        AABB a = new AABB(new Vectors2D(10, 10), new Vectors2D(20, 20));
        b.set(a);
        Vectors2D val = new Vectors2D(10, 10);
        assertEquals(val.x, b.getMin().x, 0.0);
        assertEquals(val.y, b.getMin().y, 0.0);

    }

    @Test
    public void getMax() {
        AABB b = new AABB();
        AABB a = new AABB(new Vectors2D(10, 10), new Vectors2D(20, 20));
        b.set(a);
        assertEquals(new Vectors2D(20, 20).x, b.getMax().x, 0.0);
        assertEquals(new Vectors2D(20, 20).y, b.getMax().y, 0.0);

    }

    @Test
    public void isValid() {
        AABB a = new AABB(new Vectors2D(100, 100), new Vectors2D(300, 300));
        assertTrue(a.isValid());
        a = new AABB(new Vectors2D(0, 0), new Vectors2D(0, 0));
        assertTrue(a.isValid());
        a = new AABB(new Vectors2D(Double.POSITIVE_INFINITY, 0), new Vectors2D(300, 300));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(0, 0), new Vectors2D(Double.POSITIVE_INFINITY, 300));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(0, 0), new Vectors2D(1, Double.POSITIVE_INFINITY));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(0, Double.POSITIVE_INFINITY), new Vectors2D(1, 1));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(0, -Double.POSITIVE_INFINITY), new Vectors2D(1, 1));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(0, 0.0 / 0), new Vectors2D(1, 1));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(0, 0.0 / 0), new Vectors2D(0.0 / 0, 1));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(-0, -0), new Vectors2D(-0, -0));
        assertTrue(a.isValid());
        a = new AABB(new Vectors2D(-10, -10), new Vectors2D(-0, -0));
        assertTrue(a.isValid());
        a = new AABB(new Vectors2D(-0, -0), new Vectors2D(-10, -10));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(-10, -0), new Vectors2D(-10, -10));
        assertFalse(a.isValid());
        a = new AABB(new Vectors2D(-10, -0), new Vectors2D(-10, -10));
        assertFalse(a.isValid());
    }

    @Test
    public void AABBOverLap() {
        //Corner overlaps - top right
        AABB a = new AABB(new Vectors2D(100, 100), new Vectors2D(300, 300));
        AABB b = new AABB(new Vectors2D(200, 200), new Vectors2D(400, 400));
        assertTrue(AABB.AABBOverLap(a, b));

        //Corner overlaps - top left
        a = new AABB(new Vectors2D(0, 0), new Vectors2D(200, 200));
        b = new AABB(new Vectors2D(-100, 100), new Vectors2D(100, 300));
        assertTrue(AABB.AABBOverLap(a, b));

        //Corner overlaps - bottom left
        a = new AABB(new Vectors2D(0, 0), new Vectors2D(200, 200));
        b = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        assertTrue(AABB.AABBOverLap(a, b));


        //Corner overlaps - bottom right
        a = new AABB(new Vectors2D(0, 0), new Vectors2D(200, 200));
        b = new AABB(new Vectors2D(100, -100), new Vectors2D(300, 100));
        assertTrue(AABB.AABBOverLap(a, b));

        //Middle overlaps - middle left
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-150, -50), new Vectors2D(50, 50));
        assertTrue(AABB.AABBOverLap(a, b));

        //Middle overlaps - middle right
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(50, -50), new Vectors2D(150, 50));
        assertTrue(AABB.AABBOverLap(a, b));

        //Middle overlaps - middle
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-150, -50), new Vectors2D(150, 50));
        assertTrue(AABB.AABBOverLap(a, b));

        //Middle overlaps - top
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-80, -50), new Vectors2D(50, 150));
        assertTrue(AABB.AABBOverLap(a, b));

        //Middle overlaps - bottom
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-80, -150), new Vectors2D(50, 50));
        assertTrue(AABB.AABBOverLap(a, b));

        //Middle overlaps - bottom
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-80, -150), new Vectors2D(50, 150));
        assertTrue(AABB.AABBOverLap(a, b));

        //With in
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-80, -50), new Vectors2D(50, 50));
        assertTrue(AABB.AABBOverLap(a, b));

        //Quadrant 1
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-200, 200), new Vectors2D(100, 500));
        assertFalse(AABB.AABBOverLap(a, b));

        //Quadrant 2
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-80, 200), new Vectors2D(50, 500));
        assertFalse(AABB.AABBOverLap(a, b));

        //Quadrant 3
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(500, 200), new Vectors2D(570, 500));
        assertFalse(AABB.AABBOverLap(a, b));

        //Quadrant 4
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-200, -50), new Vectors2D(-150, 50));
        assertFalse(AABB.AABBOverLap(a, b));

        //Quadrant 6
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(200, -50), new Vectors2D(250, 50));
        assertFalse(AABB.AABBOverLap(a, b));

        //Quadrant 7
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-200, -2000), new Vectors2D(-100, -500));
        assertFalse(AABB.AABBOverLap(a, b));

        //Quadrant 8
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(-80, -800), new Vectors2D(50, -500));
        assertFalse(AABB.AABBOverLap(a, b));

        //Quadrant 9
        a = new AABB(new Vectors2D(-100, -100), new Vectors2D(100, 100));
        b = new AABB(new Vectors2D(500, -700), new Vectors2D(570, -500));
        assertFalse(AABB.AABBOverLap(a, b));
    }

    @Test
    public void testAABBOverLap() {
        AABB b = new AABB(new Vectors2D(100, 300), new Vectors2D(300, 100));
        Vectors2D point = new Vectors2D(100, 100);
        assertTrue(b.AABBOverLap(point));
        //Checks if its inside
        point = new Vectors2D(150, 120);
        assertTrue(b.AABBOverLap(point));

        point = new Vectors2D(100, 100);
        assertTrue(b.AABBOverLap(point));
        point = new Vectors2D(100, 300);
        assertTrue(b.AABBOverLap(point));

        //Checks if its outside
        point = new Vectors2D(50, 100);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(50, 50);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(150, 50);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(350, 50);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(350, 200);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(350, 500);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(200, 500);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(50, 500);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(50, 200);
        assertFalse(b.AABBOverLap(point));

        point = new Vectors2D(100, 500);
        assertFalse(b.AABBOverLap(point));
        point = new Vectors2D(500, 100);
        assertFalse(b.AABBOverLap(point));
    }

    @Test
    public void copy() {
        AABB a = new AABB(new Vectors2D(-10, 10), new Vectors2D(10, 10));
        AABB b = a.copy();

        assertNotSame(b,a);
        assertEquals(a.getMin().x, b.getMin().x);
        assertEquals(a.getMin().y, b.getMin().y);
        assertEquals(a.getMax().x, b.getMax().x);
        assertEquals(a.getMax().y, b.getMax().y);
    }

    @Test
    public void addOffset() {
        AABB a = new AABB(new Vectors2D(-10, 10), new Vectors2D(10, 10));
        a.addOffset(new Vectors2D(10, 10));
        assertEquals(a.getMin().x, 0.0);
        assertEquals(a.getMin().y, 20.0);
        assertEquals(a.getMax().x, 20.0);
        assertEquals(a.getMax().y, 20.0);
    }

    @Test
    public void BodyOverlap() {
        Body a = new Body(new Circle(20), 0, 0);
        Body b = new Body(new Circle(20), 0, 0);
        assertTrue(AABB.AABBOverLap(a, b));
        a.position.add(new Vectors2D(41, 0));
        assertFalse(AABB.AABBOverLap(a, b));
        a.position.add(new Vectors2D(-6, 10));
        assertTrue(AABB.AABBOverLap(a, b));
        a.position.add(new Vectors2D(-34, -38));
        assertTrue(AABB.AABBOverLap(a, b));
    }
}