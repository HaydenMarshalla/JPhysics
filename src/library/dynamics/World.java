package library.dynamics;

import library.collision.AABB;
import library.collision.Arbiter;
import library.joints.Joint;
import library.math.Vectors2D;
import testbed.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Class for creating a world with iterative solver structure.
 */
public class World {
    private Vectors2D gravity;

    /**
     * Constructor
     *
     * @param gravity The strength of gravity in the world.
     */
    public World(Vectors2D gravity) {
        this.gravity = gravity;
    }

    /**
     * Default constructor
     */
    public World() {
        gravity = new Vectors2D(0, 0);
    }

    /**
     * Sets gravity.
     *
     * @param gravity The strength of gravity in the world.
     */
    public void setGravity(Vectors2D gravity) {
        this.gravity = gravity;
    }

    public ArrayList<Body> bodies = new ArrayList<>();

    /**
     * Adds a body to the world
     *
     * @param b Body to add.
     * @return Returns the newly added body.
     */
    public Body addBody(Body b) {
        bodies.add(b);
        return b;
    }

    /**
     * Removes a body from the world.
     *
     * @param b The body to remove from the world.
     */
    public void removeBody(Body b) {
        bodies.remove(b);
    }

    public ArrayList<Joint> joints = new ArrayList<>();

    /**
     * Adds a joint to the world.
     *
     * @param j The joint to add.
     * @return Returns the joint added to the world.
     */
    public Joint addJoint(Joint j) {
        joints.add(j);
        return j;
    }

    /**
     * Removes a joint from the world.
     *
     * @param j The joint to remove from the world.
     */
    public void removeJoint(Joint j) {
        joints.remove(j);
    }

    public ArrayList<Arbiter> contacts = new ArrayList<>();

    /**
     * The main time step method for the world to conduct an iteration of the current world call this method with a desired time step value.
     *
     * @param dt Timestep
     */
    public void step(double dt) {
        contacts.clear();

        broadPhaseCheck();

        semiImplicit(dt);

        //Correct positional errors from the discrete collisions
        for (Arbiter contact : contacts) {
            contact.penetrationResolution();
        }
    }

    /**
     * Semi implicit euler integration method for the world bodies and forces.
     *
     * @param dt Timestep
     */
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

    /**
     * Applies semi-implicit euler and drag forces.
     *
     * @param dt Timestep
     */
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

    /**
     * Method to apply all forces in the world.
     *
     * @param dt Timestep
     */
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

    /**
     * Applies linear drag to a body.
     *
     * @param b Body to apply drag to.
     */
    private void applyLinearDrag(Body b) {
        double velocityMagnitude = b.velocity.length();
        double dragForceMagnitude = velocityMagnitude * velocityMagnitude * b.linearDampening;
        Vectors2D dragForceVector = b.velocity.getNormalized().scalar(-dragForceMagnitude);
        b.applyForceToCentre(dragForceVector);
    }

    /**
     * A discrete Broad phase check of collision detection.
     */
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

    /**
     * If broad phase detection check passes, a narrow phase check is conducted to determine for certain if two objects are intersecting.
     * If two objects are, arbiters of contacts found are generated
     *
     * @param a
     * @param b
     */
    private void narrowPhaseCheck(Body a, Body b) {
        Arbiter contactQuery = new Arbiter(a, b);
        contactQuery.narrowPhase();
        if (contactQuery.contactCount > 0) {
            contacts.add(contactQuery);
        }
    }


    /**
     * Clears all objects in the current world
     */
    public void clearWorld() {
        bodies.clear();
        contacts.clear();
        joints.clear();
    }

    /**
     * Applies gravitational forces between to objects (force applied to centre of body)
     */
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

    /**
     * Debug draw method for world objects.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public void drawContact(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        for (Arbiter contact : contacts) {
            Vectors2D point = contact.contacts[0];
            Vectors2D line;
            Vectors2D beginningOfLine;
            Vectors2D endOfLine;

            g.setColor(paintSettings.contactPoint);
            line = contact.contactNormal.normal().scalar(paintSettings.TANGENT_LINE_SCALAR);
            beginningOfLine = camera.convertToScreen(point.addi(line));
            endOfLine = camera.convertToScreen(point.subtract(line));
            g.draw(new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y));

            line = contact.contactNormal.scalar(paintSettings.NORMAL_LINE_SCALAR);
            beginningOfLine = camera.convertToScreen(point.addi(line));
            endOfLine = camera.convertToScreen(point.subtract(line));
            g.draw(new Line2D.Double(beginningOfLine.x, beginningOfLine.y, endOfLine.x, endOfLine.y));
        }
    }
}