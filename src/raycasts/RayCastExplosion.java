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
    public void update() {

    }

    @Override
    public void applyBlastImpulse(double blastPower) {
    }

    @Override
    public void draw(Graphics g, ColourSettings paintSettings, Camera camera) {

    }
}
