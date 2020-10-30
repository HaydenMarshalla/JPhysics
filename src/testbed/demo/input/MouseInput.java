package testbed.demo.input;

import library.explosions.ParticleExplosion;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.ParticleExplosionTest;
import testbed.demo.tests.ProximityExplosionTest;
import testbed.demo.tests.Raycast;
import testbed.demo.tests.RaycastExplosionTest;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ConcurrentModificationException;

public class MouseInput extends TestbedControls implements MouseListener {
    public MouseInput(TestBedWindow testBedWindow) {
        super(testBedWindow);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (ProximityExplosionTest.active) {
            setProximityEpicentre(e);
            ProximityExplosionTest.p.applyBlastImpulse(5000000);
        } else if (ParticleExplosionTest.active) {
            generateParticleExplosion(e);
        } else if (RaycastExplosionTest.active) {
            RaycastExplosionTest.r.applyBlastImpulse(5000000);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

