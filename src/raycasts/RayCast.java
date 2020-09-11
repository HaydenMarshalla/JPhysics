package raycasts;

import library.Camera;
import library.ColourSettings;
import library.World;
import library.math.Vectors2D;

import java.awt.*;

public abstract class RayCast {
    public Vectors2D epicentre;
    public World world;

    public RayCast(Vectors2D location, World world) {
        epicentre = location;
        this.world = world;
    }

 /*   public void applyRaycastBlast(double force) {
        force /= n;
        for (q p : polySides) {
            body B = p.b;
            Vectors2D r = new Vectors2D(p.y, p.z).subtract(B.position);
            Vectors2D impulse = r.neg().scalar(force);
            B.velocity = B.velocity.add(impulse.scalar(B.inverseM));
            B.angularVelocity += B.inverseInertia * r.crossProduct(impulse);
        }
    }*/

    public abstract void applyBlastImpulse(double blastPower);

    public abstract void draw(Graphics g, ColourSettings paintSettings, Camera camera);
}