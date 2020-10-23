package library.math;

public class Matrix2D {
    public Vectors2D col1 = new Vectors2D();
    public Vectors2D col2 = new Vectors2D();

    public Matrix2D() {
    }

    public Matrix2D(double radians) {
        this.set(radians);
    }

    public void set(double radians) {
        double c = StrictMath.cos(radians);
        double s = StrictMath.sin(radians);

        col1.x = c;
        col1.y = -s;
        col2.x = s;
        col2.y = c;
    }

    public void set(Matrix2D m) {
        col1.x = m.col1.x;
        col1.y = m.col1.y;
        col2.x = m.col2.x;
        col2.y = m.col2.y;
    }

    public Matrix2D transpose() {
        Matrix2D mat = new Matrix2D();
        mat.col1.x = col1.x;
        mat.col1.y = col2.x;
        mat.col2.x = col1.y;
        mat.col2.y = col2.y;
        return mat;
    }

    public Vectors2D mul(Vectors2D v) {
        double x = v.x;
        double y = v.y;
        v.x = col1.x * x + col1.y * y;
        v.y = col2.x * x + col2.y * y;
        return v;
    }

    public Vectors2D mul(Vectors2D v, Vectors2D out) {
        out.x = col1.x * v.x + col1.y * v.y;
        out.y = col2.x * v.x + col2.y * v.y;
        return out;
    }

    public static void main(String[] args) {
        Vectors2D test = new Vectors2D(5, 0);
        Matrix2D m = new Matrix2D();
        m.set(0);
        System.out.println(m);
        m.mul(test);
        System.out.println(test);
    }

    @Override
    public String toString() {
        return col1.x + " : " + col1.y + "\n" + col2.x + " : " + col2.y;
    }
}
