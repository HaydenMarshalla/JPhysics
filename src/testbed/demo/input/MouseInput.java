package testbed.demo.input;

import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener {
    private TestBedWindow testBed;

    public MouseInput(TestBedWindow testBedWindow) {
        testBed = testBedWindow;
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

