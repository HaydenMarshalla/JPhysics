package raycasts;

import library.Camera;
import library.ColourSettings;
import library.World;
import library.math.Vectors2D;

import java.awt.*;

public class RayCastExplosion extends RayCast {
    private int n;

    public RayCastExplosion(Vectors2D centrePoint, World world, int n) {
        super(centrePoint, world);
        this.n = n;
    }

    public void setN(int rays){
        n = rays;

    }
    @Override
    public void applyBlastImpulse(double blastPower) {
        double force = n;
      /*  for (q p : polySides) {
            body B = p.b;
            Vectors2D r = new Vectors2D(p.y, p.z).subtract(B.position);
            Vectors2D impulse = r.neg().scalar(force);
            B.velocity = B.velocity.add(impulse.scalar(B.inverseM));
            B.angularVelocity += B.inverseInertia * r.crossProduct(impulse);
        }*/
    }

    @Override
    public void draw(Graphics g, ColourSettings paintSettings, Camera camera) {

    }


}
