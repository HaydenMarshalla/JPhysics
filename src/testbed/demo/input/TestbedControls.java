package testbed.demo.input;

import library.explosions.ParticleExplosion;
import library.explosions.ProximityExplosion;
import library.math.Vectors2D;
import library.dynamics.Settings;
import testbed.Camera;
import testbed.demo.TestBedWindow;
import testbed.demo.tests.*;

import java.awt.event.MouseEvent;

public abstract class TestbedControls {
    protected final TestBedWindow TESTBED;
    protected final Camera CAMERA;
    protected static String currentDemo;

    public TestbedControls(TestBedWindow testBedWindow) {
        this.TESTBED = testBedWindow;
        this.CAMERA = testBedWindow.getCamera();
        currentDemo = "Chains";
    }

    public void loadDemo(String demo) {
        currentDemo = demo;
        TESTBED.clearTestbedObjects();
        resetUniqueEventHandlers();
        switch (currentDemo) {
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
                TESTBED.followPayload = true;
                TESTBED.setCurrentDemo(6);
            }
            case "Slice objects" -> {
                SliceObjects.load(TESTBED);
                Settings.HERTZ = 120;
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
        TESTBED.followPayload = false;
        ProximityExplosionTest.active = false;
        ParticleExplosionTest.active = false;
        RaycastExplosionTest.active = false;
        SliceObjects.active = false;
        LineOfSight.active = false;
        Trebuchet.active = false;
        Settings.HERTZ = 60;
    }

    protected void setProximityEpicentre(MouseEvent e) {
        Vectors2D v = CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY()));
        ProximityExplosion p = (ProximityExplosion) TESTBED.getRayExplosions().get(0);
        p.setEpicentre(v);
    }

    protected void generateParticleExplosion(MouseEvent e) {
        ParticleExplosion p = new ParticleExplosion(CAMERA.convertToWorld(new Vectors2D(e.getX(), e.getY())), 100, 10);
        p.createParticles(0.5, 100, 5, TESTBED.getWorld());
        p.applyBlastImpulse(100);
        TESTBED.add(p, 2);
    }
}