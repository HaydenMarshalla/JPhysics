package testbed.demo.input;

import testbed.Camera;
import testbed.demo.TestBedWindow;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseScroll implements MouseWheelListener {
    private final TestBedWindow TESTBED;
    private final Camera CAMERA;

    public MouseScroll(TestBedWindow testBedWindow) {
        this.TESTBED = testBedWindow;
        this.CAMERA = testBedWindow.getCamera();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            CAMERA.zoom *= 0.9;
        } else {
            CAMERA.zoom *= 1.1;
        }
    }
}
