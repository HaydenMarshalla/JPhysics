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
            case "Monochromatic" -> TESTBED.PAINT_SETTINGS.monochromaticColourScheme();
            case "Display Grid" -> TESTBED.PAINT_SETTINGS.setDrawGrid(!TESTBED.PAINT_SETTINGS.getDrawGrid());
            case "Display Shapes" -> TESTBED.PAINT_SETTINGS.setDrawShapes(!TESTBED.PAINT_SETTINGS.getDrawShapes());
            case "Display Joints" -> TESTBED.PAINT_SETTINGS.setDrawJoints(!TESTBED.PAINT_SETTINGS.getDrawJoints());
            case "Display AABBs" -> TESTBED.PAINT_SETTINGS.setDrawAABBs(!TESTBED.PAINT_SETTINGS.getDrawAABBs());
            case "Display Contacts" -> TESTBED.PAINT_SETTINGS.setDrawContacts(!TESTBED.PAINT_SETTINGS.getDrawContacts());
            case "Display COMs" -> TESTBED.PAINT_SETTINGS.setDrawCOMs(!TESTBED.PAINT_SETTINGS.getDrawCOMs());
        }
    }
}