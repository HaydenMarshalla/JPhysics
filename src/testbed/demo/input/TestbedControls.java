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

    protected Vectors2D findWorldPosition(MouseEvent e) {
        return CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY()));
    }

    protected void setProximityEpicentre(MouseEvent e) {
        Vectors2D v = findWorldPosition(e);
        ProximityExplosion p = (ProximityExplosion) TESTBED.getProximityExp().get(0);
        p.changeEpicentre(v);
    }

    protected void generateParticleExplosion(MouseEvent e) {
        ParticleExplosion p = new ParticleExplosion(findWorldPosition(e), TESTBED.getWorld(), 100, 10);
        p.createParticles(0.1, 100000, 10);
        p.applyBlastImpulse(100);
        TESTBED.add(p, 1);
    }
}
