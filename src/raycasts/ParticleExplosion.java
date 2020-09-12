package raycasts;

import library.*;
import library.math.Matrix2D;
import library.math.Vectors2D;

import java.awt.*;
import java.util.ArrayList;

public class ParticleExplosion extends RayCast {
    public ArrayList<Body> particles = new ArrayList<>();
    private final int noOfParticles;

    public ParticleExplosion(Vectors2D location, World world, int noOfParticles) {
        super(location, world);
        this.noOfParticles = noOfParticles;
    }

    public void createParticles(int spacing, int size) {
        double no = 6.28319 / noOfParticles;
        Vectors2D line = new Vectors2D(0, spacing);
        Matrix2D rotate = new Matrix2D();
        rotate.set(no);
        for (int i = 0; i < noOfParticles; i++) {
            rotate.mul(line);
            Vectors2D position = epicentre.addi(line);
            Body b = new Body(new Circle(size), position.x, position.y);
            b.setGravityEffect(false);
            world.addBody(b);
            particles.add(b);
        }
    }

    @Override
    public void applyBlastImpulse(double blastPower) {
        for (Body b : particles) {
            Vectors2D blastDir = b.position.subtract(epicentre);
            double distance = blastDir.length();
            if (distance == 0) return;

            double invDistance = 1 / distance;
            double impulseMag = blastPower * invDistance;

            Vectors2D force = b.force.addi(blastDir.normalize().scalar(impulseMag));
            b.velocity.add(force.scalar(b.invMass));
        }
    }

    @Override
    public void draw(Graphics g, ColourSettings paintSettings, Camera camera) {

    }
}
