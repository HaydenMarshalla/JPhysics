package testbed.demo.input;

import testbed.demo.TestBedWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColourMenuInput extends TestbedControls implements ActionListener {
    public ColourMenuInput(TestBedWindow testBedWindow) {
        super(testBedWindow);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "Default" -> TESTBED.PAINT_SETTINGS.defaultColourScheme();
            case "Box2d" -> TESTBED.PAINT_SETTINGS.box2dColourScheme();
            case "MatterJs" -> TESTBED.PAINT_SETTINGS.matterjsColourScheme();
            case "Monochromatic" -> TESTBED.PAINT_SETTINGS.monochromaticColourScheme();
        }
    }
}