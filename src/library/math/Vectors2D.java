package library.math;

public class Vectors2D {
    public double x;
    public double y;

    public Vectors2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vectors2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vectors2D(Vectors2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vectors2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vectors2D set(Vectors2D v1) {
        this.x = v1.x;
        this.y = v1.y;
        return this;
    }

    public Vectors2D copy() {
        return new Vectors2D(this.x, this.y);
    }

    public Vectors2D negative() {
        this.x = -x;
        this.y = -y;
        return this;
    }

    public Vectors2D negativeVec() {
        return new Vectors2D(-x, -y);
    }

    public Vectors2D add(Vectors2D v) {
        this.x = x + v.x;
        this.y = y + v.y;
        return this;
    }

    public Vectors2D addi(Vectors2D v) {
        return new Vectors2D(x + v.x, y + v.y);
    }

    public Vectors2D normal() {
        return new Vectors2D(-y, x);
    }

    public Vectors2D normalize() {
        double d = Math.sqrt(x * x + y * y);
        if (d == 0) {
            d = 1;
        }
        this.x /= d;
        this.y /= d;
        return this;
    }

    public double distance(Vectors2D v) {
        double dx = this.x - v.x;
        double dy = this.y - v.y;
        return StrictMath.sqrt(dx * dx + dy * dy);
    }

    public Vectors2D subtract(Vectors2D v1) {
        return new Vectors2D(this.x - v1.x, this.y - v1.y);
    }

    public double crossProduct(Vectors2D v1) {
        return this.x * v1.y - this.y * v1.x;
    }

    public Vectors2D crossProduct(double a) {
        return this.normal().scalar(a);
    }

    public Vectors2D scalar(double a) {
        return new Vectors2D(x * a, y * a);
    }

    public double dotProduct(Vectors2D v1) {
        return v1.x * this.x + v1.y * this.y;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);

    }

    public static Vectors2D cross(Vectors2D a, double s) {
        return new Vectors2D(s * a.y, -s * a.x);
    }

    public static Vectors2D cross(double s, Vectors2D a) {
        return new Vectors2D(-s * a.y, s * a.x);
    }

    public final boolean isValid() {
        return !Double.isNaN(x) && !Double.isInfinite(x) && !Double.isNaN(y) && !Double.isInfinite(y);
    }

    public static Vectors2D[] createArray(int n) {
        Vectors2D[] array = new Vectors2D[n];
        for (Vectors2D v : array) {
            v = new Vectors2D();
        }
        return array;
    }

    public static boolean checkDifference(Vectors2D position, Vectors2D epicentre) {
        return position.x == epicentre.x && position.y == epicentre.y;
    }

    @Override
    public String toString() {
        return this.x + " : " + this.y;
    }
}
