package testbed.demo.input;

import library.math.Vectors2D;
import testbed.Camera;
import testbed.demo.TestBedWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener {
    private final TestBedWindow TESTBED;
    private final Camera CAMERA;

    public MouseInput(TestBedWindow testBedWindow) {
        this.TESTBED = testBedWindow;
        this.CAMERA = testBedWindow.getCamera();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vectors2D clickPosition = new Vectors2D(e.getX(), e.getY());
        //TO DO: screen to world
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

