package testbed.demo.input;

import library.math.Vectors2D;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DemoMenuInput extends TestbedControls implements ActionListener {
    public DemoMenuInput(TestBedWindow testBedWindow) {
        super(testBedWindow);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        TESTBED.clearTestbedObjects();
        resetUniqueEventHandlers();
        switch (event.getActionCommand()) {
            case "Chains" -> {
                Chains.load(TESTBED);
                TESTBED.setCurrentDemo(0);
            }
            case "Line of sight" -> {
                LineOfSight.load(TESTBED);
                TESTBED.setCurrentDemo(1);
            }
            case "Particle explosion" -> {
                ParticleExplosionTest.load(TESTBED);
                TESTBED.setCurrentDemo(2);
            }
            case "Proximity explosion" -> {
                ProximityExplosionTest.load(TESTBED);
                TESTBED.setCurrentDemo(3);
            }
            case "Raycast explosion" -> {
                RaycastExplosionTest.load(TESTBED);
                TESTBED.setCurrentDemo(4);
            }
            case "Raycast" -> {
                Raycast.load(TESTBED);
                TESTBED.setCurrentDemo(5);
            }
            case "Trebuchet" -> {
                Trebuchet.load(TESTBED);
                TESTBED.setCurrentDemo(6);
            }
            case "Slice objects" -> {
                SliceObjects.load(TESTBED);
                TESTBED.setCurrentDemo(7);
            }
            case "Bouncing ball" -> {
                BouncingBall.load(TESTBED);
                TESTBED.setCurrentDemo(8);
            }
            case "Mixed shapes" -> {
                MixedShapes.load(TESTBED);
                TESTBED.setCurrentDemo(9);
            }
            case "Newtons cradle" -> {
                NewtonsCradle.load(TESTBED);
                TESTBED.setCurrentDemo(10);
            }
            case "Wrecking ball" -> {
                WreckingBall.load(TESTBED);
                TESTBED.setCurrentDemo(11);
            }
            case "Friction" -> {
                Friction.load(TESTBED);
                TESTBED.setCurrentDemo(12);
            }
            case "Drag" -> {
                Drag.load(TESTBED);
                TESTBED.setCurrentDemo(13);
            }
            case "Restitution" -> {
                Restitution.load(TESTBED);
                TESTBED.setCurrentDemo(14);
            }
            case "Stacked objects" -> {
                StackedObjects.load(TESTBED);
                TESTBED.setCurrentDemo(15);
            }
        }
    }

    private void resetUniqueEventHandlers() {
        TESTBED.setCamera(new Vectors2D(0, 0), 1);
        ProximityExplosionTest.active = false;
        ParticleExplosionTest.active = false;
        RaycastExplosionTest.active = false;
        SliceObjects.active =false;
        LineOfSight.active = false;
        Trebuchet.active = false;
    }
}
