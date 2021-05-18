package testbed.demo.input;

import library.rays.Slice;
import library.math.Vectors2D;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.ParticleExplosionTest;
import testbed.demo.tests.ProximityExplosionTest;
import testbed.demo.tests.RaycastExplosionTest;
import testbed.demo.tests.SliceObjects;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput extends TestbedControls implements MouseListener {
    public MouseInput(TestBedWindow testBedWindow) {
        super(testBedWindow);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            CAMERA.setPointClicked(CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY())));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            if (ProximityExplosionTest.active) {
                setProximityEpicentre(e);
                ProximityExplosionTest.p.applyBlastImpulse(5000000);
            } else if (ParticleExplosionTest.active) {
                generateParticleExplosion(e);
            } else if (RaycastExplosionTest.active) {
                RaycastExplosionTest.r.applyBlastImpulse(500000);
            } else if (SliceObjects.active) {
                if (TESTBED.getSlicesSize() == 1) {
                    TESTBED.getSlices().get(0).setDirection(CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY())));
                    TESTBED.getSlices().get(0).sliceObjects(TESTBED.getWorld());
                    TESTBED.getSlices().clear();
                } else {
                    Slice s = new Slice(CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY())), new Vectors2D(1, 0), 0);
                    TESTBED.add(s);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

