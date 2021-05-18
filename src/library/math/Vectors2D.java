package library.math;

/**
 * 2D Vectors class
 */
public class Vectors2D {
    public double x;
    public double y;

    /**
     * Default constructor - x/y initialised to zero.
     */
    public Vectors2D() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor.
     *
     * @param x Sets x value.
     * @param y Sets y value.
     */
    public Vectors2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     *
     * @param vector Vector to copy.
     */
    public Vectors2D(Vectors2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    /**
     * Constructs a normalised direction vector.
     *
     * @param direction Direction in radians.
     */
    public Vectors2D(double direction) {
        this.x = Math.cos(direction);
        this.y = Math.sin(direction);
    }

    /**
     * Sets a vector to equal an x/y value and returns this.
     *
     * @param x x value.
     * @param y y value.
     * @return The current instance vector.
     */
    public Vectors2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets a vector to another vector and returns this.
     *
     * @param v1 Vector to set x/y values to.
     * @return The current instance vector.
     */
    public Vectors2D set(Vectors2D v1) {
        this.x = v1.x;
        this.y = v1.y;
        return this;
    }

    /**
     * Copy method to return a new copy of the current instance vector.
     *
     * @return A new Vectors2D object.
     */
    public Vectors2D copy() {
        return new Vectors2D(this.x, this.y);
    }

    /**
     * Negates the current instance vector and return this.
     *
     * @return Return the negative form of the instance vector.
     */
    public Vectors2D negative() {
        this.x = -x;
        this.y = -y;
        return this;
    }

    /**
     * Negates the current instance vector and return this.
     *
     * @return Returns a new negative vector of the current instance vector.
     */
    public Vectors2D negativeVec() {
        return new Vectors2D(-x, -y);
    }

    /**
     * Adds a vector to the current instance and return this.
     *
     * @param v Vector to add.
     * @return Returns the current instance vector.
     */
    public Vectors2D add(Vectors2D v) {
        this.x = x + v.x;
        this.y = y + v.y;
        return this;
    }

    /**
     * Adds a vector and the current instance vector together and returns a new vector of them added together.
     *
     * @param v Vector to add.
     * @return Returns a new Vectors2D of the sum of the addition of the two vectors.
     */
    public Vectors2D addi(Vectors2D v) {
        return new Vectors2D(x + v.x, y + v.y);
    }

    /**
     * Generates a normal of a vector. Normal facing to the right clock wise 90 degrees.
     *
     * @return A normal of the current instance vector.
     */
    public Vectors2D normal() {
        return new Vectors2D(-y, x);
    }

    /**
     * Normalizes the current instance vector to length 1 and returns this.
     *
     * @return Returns the normalized version of the current instance vector.
     */
    public Vectors2D normalize() {
        double d = Math.sqrt(x * x + y * y);
        if (d == 0) {
            d = 1;
        }
        this.x /= d;
        this.y /= d;
        return this;
    }

    /**
     * Finds the normalised version of a vector and returns a new vector of it.
     *
     * @return A normalized vector of the current instance vector.
     */
    public Vectors2D getNormalized() {
        double d = Math.sqrt(x * x + y * y);

        if (d == 0) {
            d = 1;
        }
        return new Vectors2D(x / d, y / d);
    }

    /**
     * Finds the distance between two vectors.
     *
     * @param v Vector to find distance from.
     * @return Returns distance from vector v to the current instance vector.
     */
    public double distance(Vectors2D v) {
        double dx = this.x - v.x;
        double dy = this.y - v.y;
        return StrictMath.sqrt(dx * dx + dy * dy);
    }

    /**
     * Subtract a vector from the current instance vector.
     *
     * @param v1 Vector to subtract.
     * @return Returns a new Vectors2D with the subtracted vector applied
     */
    public Vectors2D subtract(Vectors2D v1) {
        return new Vectors2D(this.x - v1.x, this.y - v1.y);
    }

    /**
     * Finds cross product between two vectors.
     *
     * @param v1 Other vector to apply cross product to
     * @return double
     */
    public double crossProduct(Vectors2D v1) {
        return this.x * v1.y - this.y * v1.x;
    }

    public Vectors2D crossProduct(double a) {
        return this.normal().scalar(a);
    }

    public Vectors2D scalar(double a) {
        return new Vectors2D(x * a, y * a);
    }

    /**
     * Finds dotproduct between two vectors.
     *
     * @param v1 Other vector to apply dotproduct to.
     * @return double
     */
    public double dotProduct(Vectors2D v1) {
        return v1.x * this.x + v1.y * this.y;
    }

    /**
     * Gets the length of instance vector.
     *
     * @return double
     */
    public double length() {
        return Math.sqrt(x * x + y * y);

    }

    /**
     * Static method for any cross product, same as
     * {@link #cross(double, Vectors2D)}
     *
     * @param s double.
     * @param a Vectors2D.
     * @return Cross product scalar result.
     */
    public static Vectors2D cross(Vectors2D a, double s) {
        return new Vectors2D(s * a.y, -s * a.x);
    }

    /**
     * Finds the cross product of a scalar and a vector. Produces a scalar in 2D.
     *
     * @param s double.
     * @param a Vectors2D.
     * @return Cross product scalar result.
     */
    public static Vectors2D cross(double s, Vectors2D a) {
        return new Vectors2D(-s * a.y, s * a.x);
    }

    /**
     * Checks to see if a vector has valid values set for x and y.
     *
     * @return boolean value whether a vector is valid or not.
     */
    public final boolean isValid() {
        return !Double.isNaN(x) && !Double.isInfinite(x) && !Double.isNaN(y) && !Double.isInfinite(y);
    }

    /**
     * Checks to see if a vector is set to (0,0).
     *
     * @return boolean value whether the vector is set to (0,0).
     */
    public boolean isZero() {
        return Math.abs(this.x) == 0 && Math.abs(this.y) == 0;
    }

    /**
     * Generates an array of length n with zero initialised vectors.
     *
     * @param n Length of array.
     * @return A Vectors2D array of zero initialised vectors.
     */
    public static Vectors2D[] createArray(int n) {
        Vectors2D[] array = new Vectors2D[n];
        for (Vectors2D v : array) {
            v = new Vectors2D();
        }
        return array;
    }

    @Override
    public String toString() {
        return this.x + " : " + this.y;
    }
}
