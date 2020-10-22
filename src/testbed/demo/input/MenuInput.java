package testbed.demo.input;

import testbed.Camera;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInput implements ActionListener {
    private final TestBedWindow TESTBED;
    private final Camera CAMERA;

    public MenuInput(TestBedWindow testBedWindow) {
        this.TESTBED = testBedWindow;
        this.CAMERA = testBedWindow.getCamera();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        TESTBED.clearTestbedObjects();
        if (event.getActionCommand().equals("Bouncing ball")) {
            BouncingBall.load(TESTBED);
        } else if (event.getActionCommand().equals("Car")) {
            Car.load(TESTBED);
        } else if (event.getActionCommand().equals("Chains")) {
            Chains.load(TESTBED);
        } else if (event.getActionCommand().equals("Compound bodies")) {
            CompoundBodies.load(TESTBED);
        } else if (event.getActionCommand().equals("Drag")) {
            Drag.load(TESTBED);
        } else if (event.getActionCommand().equals("Friction")) {
            Friction.load(TESTBED);
        } else if (event.getActionCommand().equals("Line of sight")) {
            LineOfSight.load(TESTBED);
        } else if (event.getActionCommand().equals("Mixed shapes")) {
            MixedShapes.load(TESTBED);
        } else if (event.getActionCommand().equals("Newtons cradle")) {
            NewtonsCradle.load(TESTBED);
        } else if (event.getActionCommand().equals("Particle explosion")) {
            ParticleExplosionTest.load(TESTBED);
        } else if (event.getActionCommand().equals("Proximity explosion")) {
            ProximityExplosionTest.load(TESTBED);
        } else if (event.getActionCommand().equals("Raycast explosion")) {
            RaycastExplosionTest.load(TESTBED);
        } else if (event.getActionCommand().equals("Raycast")) {
            Raycast.load(TESTBED);
        } else if (event.getActionCommand().equals("Restitution")) {
            Restitution.load(TESTBED);
        } else if (event.getActionCommand().equals("Stacked objects")) {
            StackedObjects.load(TESTBED);
        } else if (event.getActionCommand().equals("Trebuchet")) {
            Trebuchet.load(TESTBED);
        } else if (event.getActionCommand().equals("Wrecking ball")) {
            WreckingBall.load(TESTBED);
        }
    }
}
