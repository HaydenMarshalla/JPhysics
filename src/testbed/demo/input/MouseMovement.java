package testbed.demo.input;

import library.explosions.ProximityExplosion;
import library.explosions.RaycastExplosion;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.LineOfSight;
import testbed.demo.tests.ProximityExplosionTest;
import testbed.demo.tests.RaycastExplosionTest;

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
        if (KeyBoardInput.shift) {

        } else {
            Vectors2D v = findWorldPosition(e);
            if (ProximityExplosionTest.active) {
                ProximityExplosion p = (ProximityExplosion) TESTBED.getRayExplosions().get(0);
                p.changeEpicentre(v);
            } else if (RaycastExplosionTest.active) {
                RaycastExplosionTest.r.changeEpicentre(v);
            } else if (LineOfSight.active) {
                LineOfSight.b.setStartPoint(v);
            }
        }
    }
}
