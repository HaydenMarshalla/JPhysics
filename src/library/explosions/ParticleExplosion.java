package library.explosions;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.math.Matrix2D;
import library.math.Vectors2D;

public class ParticleExplosion {
    private final int noOfParticles;
    private final World world;
    private Vectors2D epicentre;
    private double seperationAngle;
    private final Body[] particles;

    public Body[] getParticles() {
        return particles;
    }

    public ParticleExplosion(Vectors2D epicentre, World world, int noOfParticles) {
        this.epicentre = epicentre;
        this.noOfParticles = noOfParticles;
        this.world = world;
        particles = new Body[noOfParticles];
        seperationAngle = 6.28319 / noOfParticles;
    }

    public void createParticles(int size, int density, int distance) {
        Vectors2D distanceFromCentre = new Vectors2D(0, distance);
        Matrix2D rotate = new Matrix2D(seperationAngle);
        for (int i = 0; i < noOfParticles; i++) {
            Vectors2D particlePlacement = epicentre.addi(distanceFromCentre);
            Body b = new Body(new Circle(size), particlePlacement.x, particlePlacement.y);
            b.setDensity(density);
            b.restitution = 1;
            b.staticFriction = 0;
            b.dynamicFriction = 0;
            b.affectedByGravity = false;
            world.addBody(b);
            particles[i] = b;
            rotate.mul(distanceFromCentre);
        }
    }

    public void applyBlastImpulse(double power) {
        Vectors2D line = new Vectors2D(0, 1);
        Matrix2D rotate = new Matrix2D();
        rotate.set(seperationAngle);
        for (Body b : particles) {
            b.velocity.set(line.scalar(power));
            rotate.mul(line);
        }
    }
}
