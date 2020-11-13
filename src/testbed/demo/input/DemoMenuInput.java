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
        resetUniquieEventHandlers();
        switch (event.getActionCommand()) {
            case "Bouncing ball" -> BouncingBall.load(TESTBED);
            case "Car" -> Car.load(TESTBED);
            case "Chains" -> Chains.load(TESTBED);
            case "Compound bodies" -> CompoundBodies.load(TESTBED);
            case "Drag" -> Drag.load(TESTBED);
            case "Friction" -> Friction.load(TESTBED);
            case "Line of sight" -> LineOfSight.load(TESTBED);
            case "Mixed shapes" -> MixedShapes.load(TESTBED);
            case "Newtons cradle" -> NewtonsCradle.load(TESTBED);
            case "Particle explosion" -> ParticleExplosionTest.load(TESTBED);
            case "Proximity explosion" -> ProximityExplosionTest.load(TESTBED);
            case "Raycast explosion" -> RaycastExplosionTest.load(TESTBED);
            case "Raycast" -> Raycast.load(TESTBED);
            case "Restitution" -> Restitution.load(TESTBED);
            case "Stacked objects" -> StackedObjects.load(TESTBED);
            case "Trebuchet" -> Trebuchet.load(TESTBED);
            case "Wrecking ball" -> WreckingBall.load(TESTBED);
        }
    }

    private void resetUniquieEventHandlers() {
        TESTBED.setCamera(new Vectors2D(0, 0), 1);
        ProximityExplosionTest.active = false;
        ParticleExplosionTest.active = false;
        RaycastExplosionTest.active = false;
    }
}
