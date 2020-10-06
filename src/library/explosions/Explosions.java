package library.explosions;

import library.dynamics.Body;
import library.math.Vectors2D;

import java.util.ArrayList;

public abstract class Explosions {
    public Vectors2D epicentre;

    public Explosions(Vectors2D centrePoint) {
        this.epicentre = centrePoint;
    }

    public ArrayList<Body> bodiesEffected = new ArrayList<>();

    public void applyBlastImpulse(double blastPower) {
        for (Body b : bodiesEffected) {
            Vectors2D blastDir = b.position.subtract(epicentre);
            double distance = blastDir.length();
            if (distance == 0) return;

            double invDistance = 1 / distance;
            double impulseMag = blastPower * invDistance;

            Vectors2D force = b.force.addi(blastDir.normalize().scalar(impulseMag));
            b.velocity.add(force.scalar(b.invMass));
        }
    }

    public void applyVelocityFromEpicentre(double velocity) {
        for (Body b : bodiesEffected) {
            Vectors2D blastDir = b.position.subtract(epicentre);
            double distance = blastDir.length();
            if (distance == 0) return;

            b.velocity.add(blastDir.normalize().scalar(velocity));
        }
    }

    public void applyTrailsToAll(int trailPoints, int skipSteps) {
        for (Body b : bodiesEffected) {
            b.addTrail(trailPoints, skipSteps);
        }
    }
}
