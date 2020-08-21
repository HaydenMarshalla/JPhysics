package testbed.demo.input;

import library.Camera;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseScroll implements MouseWheelListener {
    private final Camera camera;

    public MouseScroll(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            camera.zoom *= 0.9;
        } else {
            camera.zoom *= 1.1;
        }
    }
}
