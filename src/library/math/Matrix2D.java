package library.math;

public class Matrix2D {
    public Vectors2D row1 = new Vectors2D();
    public Vectors2D row2 = new Vectors2D();

    /**
     * Default constructor matrix [(0,0),(0,0)] by default.
     */
    public Matrix2D() {
    }

    /**
     * Constructs and sets the matrix up to be a rotation matrix that stores the angle specified in the matrix.
     * @param radians The desired angle of the rotation matrix
     */
    public Matrix2D(double radians) {
        this.set(radians);
    }

    /**
     * Sets the matrix up to be a rotation matrix that stores the angle specified in the matrix.
     * @param radians The desired angle of the rotation matrix
     */
    public void set(double radians) {
        double c = StrictMath.cos(radians);
        double s = StrictMath.sin(radians);

        row1.x = c;
        row1.y = -s;
        row2.x = s;
        row2.y = c;
    }

    /**
     * Sets current object matrix to be the same as the supplied parameters matrix.
     * @param m Matrix to set current object to
     */
    public void set(Matrix2D m) {
        row1.x = m.row1.x;
        row1.y = m.row1.y;
        row2.x = m.row2.x;
        row2.y = m.row2.y;
    }

    public Matrix2D transpose() {
        Matrix2D mat = new Matrix2D();
        mat.row1.x = row1.x;
        mat.row1.y = row2.x;
        mat.row2.x = row1.y;
        mat.row2.y = row2.y;
        return mat;
    }

    public Vectors2D mul(Vectors2D v) {
        double x = v.x;
        double y = v.y;
        v.x = row1.x * x + row1.y * y;
        v.y = row2.x * x + row2.y * y;
        return v;
    }

    public Vectors2D mul(Vectors2D v, Vectors2D out) {
        out.x = row1.x * v.x + row1.y * v.y;
        out.y = row2.x * v.x + row2.y * v.y;
        return out;
    }

    public static void main(String[] args) {
        Vectors2D test = new Vectors2D(5, 0);
        Matrix2D m = new Matrix2D();
        m.set(0.5);
        m.mul(test);
        System.out.println(test);
    }

    @Override
    public String toString() {
        return row1.x + " : " + row1.y + "\n" + row2.x + " : " + row2.y;
    }
}
