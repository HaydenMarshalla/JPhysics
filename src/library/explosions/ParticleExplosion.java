package library.explosions;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.math.Matrix2D;
import library.math.Vectors2D;

/**
 * Models particle explosions.
 */
public class ParticleExplosion {
    private final int noOfParticles;
    private Vectors2D epicentre;
    private final Body[] particles;
    private double lifespan;

    /**
     * Getter to return the list of particles in the world.
     *
     * @return Array of bodies.
     */
    public Body[] getParticles() {
        return particles;
    }

    /**
     * Constructor.
     *
     * @param epicentre     Vector location of explosion epicenter.
     * @param noOfParticles Total number of particles the explosion has.
     * @param life          The life time of the particle.
     */
    public ParticleExplosion(Vectors2D epicentre, int noOfParticles, double life) {
        this.epicentre = epicentre;
        this.noOfParticles = noOfParticles;
        particles = new Body[noOfParticles];
        lifespan = life;
    }

    /**
     * Creates particles in the supplied world.
     *
     * @param size    The size of the particles.
     * @param density The density of the particles.
     * @param radius  The distance away from the epicenter the particles are placed.
     * @param world   The world the particles are created in.
     */
    public void createParticles(double size, int density, int radius, World world) {
        double separationAngle = 6.28319 / noOfParticles;
        Vectors2D distanceFromCentre = new Vectors2D(0, radius);
        Matrix2D rotate = new Matrix2D(separationAngle);
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

    /**
     * Applies a blast impulse to all particles created.
     *
     * @param blastPower The impulse magnitude.
     */
    public void applyBlastImpulse(double blastPower) {
        Vectors2D line;
        for (Body b : particles) {
            line = b.position.subtract(epicentre);
            b.velocity.set(line.scalar(blastPower));
        }
    }
}
