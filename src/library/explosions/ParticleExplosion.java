package library.explosions;

import library.dynamics.Body;
import library.dynamics.World;
import library.geometry.Circle;
import library.math.Matrix2D;
import library.math.Vectors2D;

public class ParticleExplosion extends Explosions {
    private final int noOfParticles;
    private final World world;

    public ParticleExplosion(Vectors2D epicentre, World world, int noOfParticles) {
        super(epicentre);
        this.noOfParticles = noOfParticles;
        this.world = world;
    }

    public void createParticles(int spacing, int size, int density) {
        double no = 6.28319 / noOfParticles;
        Vectors2D line = new Vectors2D(0, spacing);
        Matrix2D rotate = new Matrix2D();
        rotate.set(no);
        for (int i = 0; i < noOfParticles; i++) {
            rotate.mul(line);
            Vectors2D position = epicentre.addi(line);
            Body b = new Body(new Circle(size), position.x, position.y);
            b.setDensity(density);
            b.setGravityEffect(false);
            world.addBody(b);
            bodiesEffected.add(b);
        }
    }
}
