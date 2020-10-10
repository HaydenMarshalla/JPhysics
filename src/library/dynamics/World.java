package library.dynamics;

import library.collision.AABB;
import library.collision.Arbiter;
import library.geometry.Polygon;
import library.joints.Joint;
import library.math.Vectors2D;
import library.utils.Settings;

import java.util.ArrayList;

public class World {
    public Vectors2D gravity;

    public void setGravity(Vectors2D gravity) {
        this.gravity = gravity;
    }

    public World(Vectors2D gravity) {
        this.gravity = gravity;
    }

    public World() {
        gravity = new Vectors2D(0, 0);
    }

    public ArrayList<Body> bodies = new ArrayList<>();
    public ArrayList<Arbiter> contacts = new ArrayList<>();
    public ArrayList<Joint> joints = new ArrayList<>();

    public void step() {
        double dt = Settings.HERTZ > 0.0 ? 1.0 / Settings.HERTZ : 0.0;

        contacts.clear();

        broadPhaseCheck();

        //Applies tentative velocities
        for (Body b : bodies) {
            if (b.invMass == 0) {
                continue;
            }

            //Separated gravity integration to allow explosion particles to not be affected by gravity
            if (b.affectedByGravity) {
                b.velocity.add(gravity.scalar(dt));
            }

            b.velocity.add(b.force.scalar(b.invMass).scalar(dt));
            b.angularVelocity += dt * b.invI * b.torque;
        }

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
            Vectors2D posChange = b.velocity.scalar(dt);
            b.position.add(posChange);

            if (b.angularVelocity != 0 && b.shape instanceof Polygon) {
                b.setOrientation(b.orientation + (dt * b.angularVelocity));
            }

            b.force.set(0, 0);
            b.torque = 0;
        }

        //Correct positional errors from the discrete collisions
        for (Arbiter contact : contacts) {
            contact.penetrationResolution();
        }
    }

    private void broadPhaseCheck() {
        for (int i = 0; i < bodies.size(); i++) {
            Body a = bodies.get(i);

            for (int x = i + 1; x < bodies.size(); x++) {
                Body b = bodies.get(x);

                if (a.invMass == 0 && b.invMass == 0) {
                    continue;
                }

                if (AABB.BodyOverlapCheck(a, b)) {
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

    public Body addBody(Body b) {
        bodies.add(b);
        return b;
    }

    public void removeBody(Body b) {
        bodies.remove(b);
    }

    public Joint addJoint(Joint j) {
        joints.add(j);
        return j;
    }

    public void removeJoint(Joint j) {
        joints.remove(j);
    }

    private double dragValue;

    private void applyDrag(Body b) {
        b.force = b.velocity.scalar(-dragValue * b.mass);
    }

    public void setDragValue(double i) {
        dragValue = i;
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
}