package testbed.demo.input;

import library.explosions.ProximityExplosion;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.ProximityExplosionTest;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMovement extends TestbedControls implements MouseMotionListener {
    public MouseMovement(TestBedWindow testBedWindow) {
        super(testBedWindow);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Vectors2D v = findWorldPosition(e);
        if (ProximityExplosionTest.active) {
            ProximityExplosion p = (ProximityExplosion) TESTBED.getProximityExp().get(0);
            p.changeEpicentre(v);
        }
    }
}
