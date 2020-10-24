package testbed.demo.input;

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
}
