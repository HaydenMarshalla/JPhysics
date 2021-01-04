package testbed.demo.input;

import library.explosions.ParticleExplosion;
import library.explosions.ProximityExplosion;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.demo.TestBedWindow;

import java.awt.event.MouseEvent;

public abstract class TestbedControls {
    protected final TestBedWindow TESTBED;
    protected final Camera CAMERA;

    public TestbedControls(TestBedWindow testBedWindow) {
        this.TESTBED = testBedWindow;
        this.CAMERA = testBedWindow.getCamera();
    }

    protected void setProximityEpicentre(MouseEvent e) {
        Vectors2D v = CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY()));
        ProximityExplosion p = (ProximityExplosion) TESTBED.getRayExplosions().get(0);
        p.changeEpicentre(v);
    }

    protected void generateParticleExplosion(MouseEvent e) {
        ParticleExplosion p = new ParticleExplosion(CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY())), TESTBED.getWorld(), 100, 10);
        p.createParticles(0.1, 100000, 10);
        p.applyBlastImpulse(100);
        TESTBED.add(p, 2);
    }
}