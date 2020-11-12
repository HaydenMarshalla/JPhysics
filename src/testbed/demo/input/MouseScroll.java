package testbed.demo.input;

import testbed.demo.TestBedWindow;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseScroll extends TestbedControls implements MouseWheelListener {
    public MouseScroll(TestBedWindow testBedWindow) {
        super(testBedWindow);
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
