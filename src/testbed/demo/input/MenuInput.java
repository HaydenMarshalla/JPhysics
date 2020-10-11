package testbed.demo.input;

import testbed.demo.TestBedWindow;
import testbed.demo.tests.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInput implements ActionListener {
    private final TestBedWindow testbed;

    public MenuInput(TestBedWindow gameScreen) {
        this.testbed = gameScreen;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        testbed.clearTestbedObjects();
        if (event.getActionCommand().equals("Arched bridged")) {
            ArchedBridge.load(testbed);
        } else if (event.getActionCommand().equals("Bouncing ball")) {
            BouncingBall.load(testbed);
        } else if (event.getActionCommand().equals("Car")) {
            Car.load(testbed);
        } else if (event.getActionCommand().equals("Chains")) {
            Chains.load(testbed);
        } else if (event.getActionCommand().equals("Compound bodies")) {
            CompoundBodies.load(testbed);
        } else if (event.getActionCommand().equals("Drag")) {
            Drag.load(testbed);
        } else if (event.getActionCommand().equals("Friction")) {
            Friction.load(testbed);
        } else if (event.getActionCommand().equals("Line of sight")) {
            LineOfSight.load(testbed);
        } else if (event.getActionCommand().equals("Mixed shapes")) {
            MixedShapes.load(testbed);
        } else if (event.getActionCommand().equals("Newtons cradle")) {
            NewtonsCradle.load(testbed);
        } else if (event.getActionCommand().equals("Particle explosion")) {
            ParticleExplosion.load(testbed);
        } else if (event.getActionCommand().equals("Proximity explosion")) {
            ProximityExplosion.load(testbed);
        } else if (event.getActionCommand().equals("Raycast explosion")) {
            RaycastExplosion.load(testbed);
        } else if (event.getActionCommand().equals("Raycast")) {
            Raycast.load(testbed);
        } else if (event.getActionCommand().equals("Restitution")) {
            Restitution.load(testbed);
        } else if (event.getActionCommand().equals("Stacked objects")) {
            StackedObjects.load(testbed);
        } else if (event.getActionCommand().equals("Trebuchet")) {
            Trebuchet.load(testbed);
        } else if (event.getActionCommand().equals("Wrecking ball")) {
            WreckingBall.load(testbed);
        }
    }
}
