package testbed.demo.input;

import testbed.ColourSettings;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.Trebuchet;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardInput extends TestbedControls implements KeyListener {
    public KeyBoardInput(TestBedWindow testBedWindow) {
        super(testBedWindow);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (TESTBED.isPaused()) {
                TESTBED.resume();
            } else {
                TESTBED.pause();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_B) {
            if (TESTBED.getWorld().joints.size() == 3 && Trebuchet.active)
                TESTBED.getWorld().joints.remove(2);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_M) {
            ColourSettings p = TESTBED.getPAINT_SETTINGS();
            p.setDrawText(!p.getDrawText());
        }
    }
}

