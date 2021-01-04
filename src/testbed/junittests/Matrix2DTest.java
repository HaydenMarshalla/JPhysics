package testbed.junittests;

import library.math.Matrix2D;
import library.math.Vectors2D;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class Matrix2DTest {
    @Test
    public void setUsingRadians() {
        Matrix2D m = new Matrix2D();
        m.set(1);
        assertEquals(m.row1.x, 0.5403023058681398);
        assertEquals(m.row2.x, 0.8414709848078965);
        assertEquals(m.row1.y, -0.8414709848078965);
        assertEquals(m.row2.y, 0.5403023058681398);
    }

    @Test
    public void setUsingMatrix() {
        Matrix2D m = new Matrix2D();
        m.set(1);
        Matrix2D u = new Matrix2D();
        u.set(m);
        assertEquals(u.row1.x, m.row1.x);
        assertEquals(u.row2.x, m.row2.x);
        assertEquals(u.row1.y, m.row1.y);
        assertEquals(u.row2.y, m.row2.y);
    }

    @Test
    public void transpose() {
        Matrix2D m = new Matrix2D();
        m.set(1);
        Matrix2D u = new Matrix2D();
        u.set(m);
        assertEquals(u.row1.x, m.row1.x);
        assertEquals(u.row2.x, m.row2.x);
        assertEquals(u.row1.y, m.row1.y);
        assertEquals(u.row2.y, m.row2.y);
    }

    @Test
    public void mul() {
        Matrix2D m = new Matrix2D();
        m.set(1);
        Vectors2D v = new Vectors2D(1, 0);
        m.mul(v);
        assertEquals(v.x, 0.5403023058681398);
        assertEquals(v.y, 0.8414709848078965);
    }

    @Test
    public void testMul() {
        Matrix2D m = new Matrix2D();
        m.set(1);
        Vectors2D v = new Vectors2D(1, 0);
        Vectors2D q = new Vectors2D(10, 0);
        m.mul(v, q);
        assertEquals(q.x, 0.5403023058681398);
        assertEquals(q.y, 0.8414709848078965);
        assertEquals(v.x, 1.0);
        assertEquals(v.y, 0.0);
    }
}