package raycasts;

import library.Body;
import library.Camera;
import library.ColourSettings;
import library.World;
import library.math.Vectors2D;

import java.awt.*;
import java.util.ArrayList;

public class ProximityExplosion extends RayCast {
    public ArrayList<Body> inRangeBodies = new ArrayList<>();
    private ArrayList<Vectors2D> linesToBodies = new ArrayList<>();
    private int proximity;

    public ProximityExplosion(Vectors2D centrePoint, World world, int radius) {
        super(centrePoint, world);
        proximity = radius;
    }

    public void proximityCheck() {
        linesToBodies.clear();
        inRangeBodies.clear();
        for (Body b : world.bodies) {
            Vectors2D blastDist = b.position.subtract(epicentre);
            if (blastDist.length() < proximity) {
                inRangeBodies.add(b);
                linesToBodies.add(b.position);
            }
        }
    }

    @Override
    public void applyBlastImpulse(double blastPower) {
        for (Body b : inRangeBodies) {
            Vectors2D blastDir = b.position.subtract(epicentre);
            double distance = blastDir.length();
            if (distance == 0) return;

            double invDistance = 1 / distance;
            //Not physically correct as it should be blast * radius to object ^ 2 as the pressure of an explosion in 2D dissipates
            double impulseMag = blastPower * invDistance;

            Vectors2D force = b.force.addi(blastDir.normalize().scalar(impulseMag));
            //Has to be done here instead of adding a force otherwise it gets effected by the timestep which for the explosion would mean it only occurs during DT once but in reality and explosion has a long impulse time and so transferring all the force to velocity assumes the impulse lasted as long as dt and all momentum is conserved
            b.velocity.addi(force.scalar(b.invMass));
        }
    }

    @Override
    public void draw(Graphics g, ColourSettings paintSettings, Camera camera) {

    }
}
