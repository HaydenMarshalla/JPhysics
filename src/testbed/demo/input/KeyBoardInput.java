package testbed.demo.input;

import library.Camera;
import library.math.Vectors2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardInput implements KeyListener {
    private final Camera camera;

    private boolean shift = false;

    public KeyBoardInput(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D && shift) {
            camera.transformCentre(new Vectors2D(10, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_A && shift) {
            camera.transformCentre(new Vectors2D(-10, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_W && shift) {
            camera.transformCentre(new Vectors2D(0, 10));
        } else if (e.getKeyCode() == KeyEvent.VK_S && shift) {
            camera.transformCentre(new Vectors2D(0, -10));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = false;
        }
    }
}

