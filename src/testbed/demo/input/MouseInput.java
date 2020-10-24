package testbed.demo.input;

import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

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

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vectors2D clickPosition = new Vectors2D(e.getX(), e.getY());
        Vectors2D worldPosition = CAMERA.convertToScreen(clickPosition);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

