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
    private final Body[] particles;
    private double timePassed;

    public Body[] getParticles() {
        return particles;
    }

    public ParticleExplosion(Vectors2D epicentre, World world, int noOfParticles, double life) {
        this.epicentre = epicentre;
        this.noOfParticles = noOfParticles;
        this.world = world;
        particles = new Body[noOfParticles];
        timePassed = 0;
    }

    public void createParticles(double size, int density, int radius) {
        double seperationAngle = 6.28319 / noOfParticles;
        Vectors2D distanceFromCentre = new Vectors2D(0, radius);
        Matrix2D rotate = new Matrix2D(seperationAngle);
        for (int i = 0; i < noOfParticles; i++) {
            Vectors2D particlePlacement = epicentre.addi(distanceFromCentre);
            Body b = new Body(new Circle(size), particlePlacement.x, particlePlacement.y);
            b.setDensity(density);
            b.restitution = 1;
            b.staticFriction = 0;
            b.dynamicFriction = 0;
            b.affectedByGravity = false;
            b.linearDampening = 0;
            b.particle = true;
            world.addBody(b);
            particles[i] = b;
            rotate.mul(distanceFromCentre);
        }
    }

    public void applyBlastImpulse(double power) {
        Vectors2D line;
        for (Body b : particles) {
            line = b.position.subtract(epicentre);
            b.velocity.set(line.scalar(power));
        }
    }
}
