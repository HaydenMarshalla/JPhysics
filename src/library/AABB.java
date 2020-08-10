package library;

import library.math.Vectors2D;

public class AABB {
    /**
     * lower left vertex of bounding box
     */
    private Vectors2D min;
    /**
     * Top right vertex of bounding box.
     */
    private Vectors2D max;

    public AABB(Vectors2D min, Vectors2D max) {
        this.min = min.copy();
        this.max = max.copy();
    }

    public AABB() {
        this.min = new Vectors2D();
        this.max = new Vectors2D();
    }

    public final void set(final AABB aabb) {
        Vectors2D v = aabb.min;
        min.x = v.x;
        min.y = v.y;
        Vectors2D v1 = aabb.max;
        max.x = v1.x;
        max.y = v1.y;
    }

    public Vectors2D getMin() {
        return min;
    }

    public Vectors2D getMax() {
        return max;
    }

    public final boolean isValid() {
        if (max.x - min.x < 0) {
            return false;
        }
        if (max.y - min.y < 0) {
            return false;
        }
        return min.isValid() && max.isValid();
    }

    public boolean AABBOverLap(Vectors2D point) {
        double x = point.x;
        double y = point.y;
        return x <= this.getMax().x && x >= this.getMin().x && y >= this.getMax().y && y <= this.getMin().y;
    }

    @Override
    public final String toString() {
        return "AABB[" + min + " . " + max + "]";
    }

    public static boolean AABBOverLap(AABB a, AABB b) {
        return a.min.x <= b.max.x &&
                a.max.x >= b.min.x &&
                a.min.y <= b.max.y &&
                a.max.y >= b.min.y;
    }
}
