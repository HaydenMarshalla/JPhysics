package raycasts;

import library.*;
import library.math.Matrix2D;
import library.math.Vectors2D;

import java.awt.*;

public class ParticleExplosion extends RayCast {
    private int particles;

    public ParticleExplosion(Vectors2D centrePoint, World world, int noOfParticles) {
        super(centrePoint, world);
        this.particles = noOfParticles;
    }

    public void createParticles() {
        double no = 6.28319 / particles;
        Vectors2D line = new Vectors2D(0, 10);
        Vectors2D position = epicentre;
        Matrix2D rotate = new Matrix2D();
        rotate.set(0);
        for (int i = 0; i < particles; i++) {
            rotate.mul(line);
            position.add(line);
            world.addBody(new Body(new Circle(5), (int) position.x, (int) position.y));
            rotate.set(no);
        }
    }

    @Override
    public void applyBlastImpulse(double blastPower) {

    }

    @Override
    public void draw(Graphics g, ColourSettings paintSettings, Camera camera) {

    }
}
