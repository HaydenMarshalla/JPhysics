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

    // The main time step method for the world to conduct an iteration of the current world call this method with a desired time step value.
    public void step(double dt) {
        contacts.clear();

        broadPhaseCheck();

        semiImplicit(dt);
        //improvedEuler(dt);

        //Correct positional errors from the discrete collisions
        for (Arbiter contact : contacts) {
            contact.penetrationResolution();
        }
    }

    private void improvedEuler(double dt) {

    }

    private void semiImplicit(double dt) {
        //Applies tentative velocities
        applyForces(dt);

        solve(dt);

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
    }

    // Applies semi-implicit euler and drag forces
    private void applyForces(double dt) {
        for (Body b : bodies) {
            if (b.invMass == 0) {
                continue;
            }

            applyLinearDrag(b);

            if (b.affectedByGravity) {
                b.velocity.add(gravity.scalar(dt));
            }

            b.velocity.add(b.force.scalar(b.invMass).scalar(dt));
            b.angularVelocity += dt * b.invI * b.torque;
        }
    }

    private void solve(double dt) {
        /*
        Resolve joints
        Note: this is removed from the iterations at this stage as the application of forces is different.
        The extra iterations on joints make the forces of the joints multiple times larger equal to the number of iterations.
        Early out could be used like in the collision solver
        This may change in the future and will be revised at a later date.
        */
        for (
                Joint j : joints) {
            j.applyTension();
        }

        //Resolve collisions
        for (
                int i = 0;
                i < Settings.ITERATIONS; i++) {
            for (Arbiter contact : contacts) {
                contact.solve();
            }
        }
    }

    private void applyLinearDrag(Body b) {
        double velocityMagnitude = b.velocity.length();
        double dragForceMagnitude = velocityMagnitude * velocityMagnitude * b.linearDampening;
        Vectors2D dragForceVector = b.velocity.getNormalized().scalar(-dragForceMagnitude);
        b.applyForceToCentre(dragForceVector);
    }

    //A discrete Broad phase check of collision detection.
    private void broadPhaseCheck() {
        for (int i = 0; i < bodies.size(); i++) {
            Body a = bodies.get(i);

            for (int x = i + 1; x < bodies.size(); x++) {
                Body b = bodies.get(x);

                //Ignores static or particle objects
                if (a.invMass == 0 && b.invMass == 0 || a.particle && b.particle) {
                    continue;
                }

                if (AABB.AABBOverLap(a, b)) {
                    narrowPhaseCheck(a, b);
                }
            }
        }
    }

    // If broad phase detection check passes, a narrow phase check is conducted to determine for certain if two objects are intersecting.
    // If two objects are, arbiters of contacts found are generated
    private void narrowPhaseCheck(Body a, Body b) {
        Arbiter contactQuery = new Arbiter(a, b);
        contactQuery.narrowPhase();
        if (contactQuery.contactCount > 0) {
            contacts.add(contactQuery);
        }
    }

    //Clears all objects in the current world
    public void clearWorld() {
        bodies.clear();
        contacts.clear();
        joints.clear();
    }

    //Applies gravitational forces between to objects (force applied to centre of body)
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

    //Draw method to draw contacts between objects
    public void drawContact(Graphics2D g2d, ColourSettings paintSettings, Camera camera) {
        for (Arbiter contact : contacts) {
            Vectors2D point = contact.contacts[0];
            Vectors2D line;
            Vectors2D beginningOfLine;
            Vectors2D endOfLine;

            if (paintSettings.getDrawContactNormals()) {
                g2d.setColor(paintSettings.contactNormals);
                line = contact.normal.scalar(paintSettings.CONTACT_LINE_SCALAR);
                beginningOfLine = camera.convertToScreen(point.addi(line));
                endOfLine = camera.convertToScreen(point.subtract(line));
                g2d.draw(new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y));
            }

            if (paintSettings.getDrawContactPoints()) {
                g2d.setColor(paintSettings.contactPoint);
                line = contact.normal.normal().scalar(paintSettings.NORMAL_LINE_SCALAR);
                beginningOfLine = camera.convertToScreen(point.addi(line));
                endOfLine = camera.convertToScreen(point.subtract(line));
                g2d.draw(new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y));
            }
        }
    }
}