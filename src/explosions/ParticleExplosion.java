package explosions;

import library.*;
import library.math.Matrix2D;
import library.math.Vectors2D;

public class ParticleExplosion extends Explosions {
    private final int noOfParticles;

    public ParticleExplosion(Vectors2D location, World world, int noOfParticles) {
        super(location, world);
        this.noOfParticles = noOfParticles;
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
