package testbed.demo.input;

import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardInput extends TestbedControls implements KeyListener {
    private boolean shift = false;

    public KeyBoardInput(TestBedWindow testBedWindow) {
        super(testBedWindow);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (TESTBED.isPaused()) {
                TESTBED.resume();
            } else {
                TESTBED.pause();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D && shift) {
            CAMERA.transformCentre(new Vectors2D(10, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_A && shift) {
            CAMERA.transformCentre(new Vectors2D(-10, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_W && shift) {
            CAMERA.transformCentre(new Vectors2D(0, 10));
        } else if (e.getKeyCode() == KeyEvent.VK_S && shift) {
            CAMERA.transformCentre(new Vectors2D(0, -10));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = false;
        }
    }
}

