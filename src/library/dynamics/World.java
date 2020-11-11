package library.dynamics;

import library.collision.AABB;
import library.collision.Arbiter;
import library.joints.Joint;
import library.math.Vectors2D;
import testbed.ColourSettings;
import library.utils.Settings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class World {
    private Vectors2D gravity;

    public World(Vectors2D gravity) {
        this.gravity = gravity;
    }

    public World() {
        gravity = new Vectors2D(0, 0);
    }

    public void setGravity(Vectors2D gravity) {
        this.gravity = gravity;
    }

    public ArrayList<Body> bodies = new ArrayList<>();

    public Body addBody(Body b) {
        bodies.add(b);
        return b;
    }

    public void removeBody(Body b) {
        bodies.remove(b);
    }

    public ArrayList<Joint> joints = new ArrayList<>();

    public Joint addJoint(Joint j) {
        joints.add(j);
        return j;
    }

    public void removeJoint(Joint j) {
        joints.remove(j);
    }

    public ArrayList<Arbiter> contacts = new ArrayList<>();

    public void step(double dt) {
        contacts.clear();

        broadPhaseCheck();

        //Applies tentative velocities
        applyForces(dt);

        //Resolve collisions
        for (int i = 0; i < Settings.ITERATIONS; i++) {
            for (Arbiter contact : contacts) {
                contact.solve();
            }
            for (Joint j : joints) {
                j.applyTension();
            }
        }

        //Integrate positions
        for (Body b : bodies) {
            if (b.invMass == 0) {
                continue;
            }

            b.position.add(b.velocity.scalar(dt));
            b.setOrientation(b.orientation + (dt * b.angularVelocity));

            b.force.set(0, 0);
            b.torque = 0;
        }

        //Correct positional errors from the discrete collisions
        for (Arbiter contact : contacts) {
            //contact.penetrationResolution();
        }
    }

    private void applyLinearDrag(Body b) {
        double velocityMagnitude = b.velocity.length();
        double dragForceMagnitude =velocityMagnitude * velocityMagnitude * b.linearDampening;
        Vectors2D dragForceVector = b.velocity.getNormalized().scalar(-dragForceMagnitude);
        b.applyForceToCentre(dragForceVector);
    }

    private void applyForces(double dt) {
        for (Body b : bodies) {
            if (b.invMass == 0) {
                continue;
            }

            if (b.affectedByGravity) {
                b.applyForceToCentre(gravity);
            }

            applyLinearDrag(b);

            b.velocity.add(gravity.addi(b.force.scalar(b.invMass)).scalar(dt));
            b.angularVelocity += dt * b.invI * b.torque;
        }
    }

    private void broadPhaseCheck() {
        for (int i = 0; i < bodies.size(); i++) {
            Body a = bodies.get(i);

            for (int x = i + 1; x < bodies.size(); x++) {
                Body b = bodies.get(x);

                if (a.invMass == 0 && b.invMass == 0 || a.particle && b.particle) {
                    continue;
                }

                if (AABB.AABBOverLap(a, b)) {
                    narrowPhaseCheck(a, b);
                }
            }
        }
    }

    private void narrowPhaseCheck(Body a, Body b) {
        Arbiter contactQuery = new Arbiter(a, b);
        contactQuery.narrowPhase();
        if (contactQuery.contactCount > 0) {
            contacts.add(contactQuery);
        }
    }

    public void clearWorld() {
        bodies.clear();
        contacts.clear();
        joints.clear();
    }

    public void gravityBetweenObj() {
        for (int a = 0; a < bodies.size(); a++) {
            Body A = bodies.get(a);
            for (int b = a + 1; b < bodies.size(); b++) {
                Body B = bodies.get(b);
                double distance = A.position.distance(B.position);
                double force = Math.pow(6.67, -11) * A.mass * B.mass / (distance * distance);
                Vectors2D direction = new Vectors2D(B.position.x - A.position.x, B.position.y - A.position.y);
                direction = direction.scalar(force);
                Vectors2D oppositeDir = new Vectors2D(-direction.x, -direction.y);
                A.force.addi(direction);
                B.force.addi(oppositeDir);
            }
        }
    }

    public void drawContact(Graphics2D g2d, ColourSettings paintSettings, Camera camera) {
        g2d.setColor(paintSettings.contactPoint);
        for (Arbiter contact : contacts) {
                Vectors2D point = contact.contacts[0];
                Vectors2D line;
                Vectors2D beginningOfLine;
                Vectors2D endOfLine;

                if (paintSettings.getDrawContactNormals()) {
                    line = contact.normal.scalar(paintSettings.CONTACT_LINE_SCALAR);
                    beginningOfLine = camera.convertToScreen(point.addi(line));
                    endOfLine = camera.convertToScreen(point.subtract(line));
                    g2d.draw(new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y));
                }

                if (paintSettings.getDrawContactPoints()) {
                    line = contact.normal.normal().scalar(paintSettings.NORMAL_LINE_SCALAR);
                    beginningOfLine = camera.convertToScreen(point.addi(line));
                    endOfLine = camera.convertToScreen(point.subtract(line));
                    g2d.draw(new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y));
                }
        }
    }

}