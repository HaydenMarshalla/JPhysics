package library;

import library.math.Vectors2D;

public class Arbiter {
    Body A;
    Body B;

    Arbiter(Body a, Body b) {
        this.A = a;
        this.B = b;
    }

    public final Vectors2D[] contacts = {new Vectors2D(), new Vectors2D()};
    public Vectors2D normal = new Vectors2D();
    public int contactCount = 0;
    public double restitution;

    public void narrowPhase() {
        restitution = Math.min(A.restitution, B.restitution);
        if (A.shape instanceof Circle && B.shape instanceof Circle) {
            circleVsCircle();
        } else if (A.shape instanceof Circle && B.shape instanceof Polygon) {
            circleVsPolygon(A, B);
        } else if (A.shape instanceof Polygon && B.shape instanceof Circle) {
            circleVsPolygon(B, A);
            if (this.contactCount > 0) {
                this.normal.negative();
            }
        } else if (A.shape instanceof Polygon && B.shape instanceof Polygon) {
            polygonVsPolygon();
        }
    }

    private double penetration = 0;

    private void circleVsCircle() {
        Circle ca = (Circle) A.shape;
        Circle cb = (Circle) B.shape;

        Vectors2D normal = B.position.subtract(A.position);

        double distance = normal.length();
        double radius = ca.radius + cb.radius;

        if (distance < radius) {
            this.contactCount = 1;
            this.penetration = radius - distance;
            this.normal = normal.normalize();
            this.contacts[0].set(normal.normalize().scalar(ca.radius).add(A.position));
        }
    }

    private void circleVsPolygon(Body a, Body b) {
        Circle A = (Circle) a.shape;
        Polygon B = (Polygon) b.shape;

        //Transpose effectively removes the rotation thus allowing the OBB vs OBB detection to become AABB vs OBB
        Vectors2D distanceOfPoints = a.position.subtract(b.position);
        Vectors2D distancePolyToCircle = B.orient.transpose().mul(distanceOfPoints);
        double penetration = -Double.MAX_VALUE;
        int faceNormal = 0;
        //Applies SAT to check for penetration
        for (int i = 0; i < B.vertices.length; i++) {
            double distance = B.normals[i].dotProduct(distancePolyToCircle.subtract(B.vertices[i]));

            //If circle is outside of polygon, no collision detected.
            if (distance > A.radius) {
                return;
            }

            if (distance > penetration) {
                faceNormal = i;
                penetration = distance;
            }
        }
        //Get vertex's of face to be evaluated
        Vectors2D vector1 = B.vertices[faceNormal];
        Vectors2D vector2 = B.vertices[faceNormal + 1 < B.vertices.length ? faceNormal + 1 : 0];

        double firstPolyCorner = (distancePolyToCircle.subtract(vector1)).dotProduct(vector2.subtract(vector1));
        double secondPolyCorner = (distancePolyToCircle.subtract(vector2)).dotProduct(vector1.subtract(vector2));

        if (firstPolyCorner <= 0.0) {
            penetration = distancePolyToCircle.distance(vector1);
            if (penetration >= A.radius) {
                return;
            }

            this.penetration = penetration;
            contactCount = 1;
            B.orient.mul(normal.set(vector1.subtract(distancePolyToCircle).normalize()));
            contacts[0] = B.orient.mul(vector1, new Vectors2D()).add(b.position);
        } else if (secondPolyCorner <= 0.0) {
            penetration = distancePolyToCircle.distance(vector2);
            if (penetration >= A.radius) {
                return;
            }
            this.penetration = penetration;
            contactCount = 1;
            B.orient.mul(normal.set(vector2.subtract(distancePolyToCircle).normalize()));
            contacts[0] = B.orient.mul(vector2, new Vectors2D()).add(b.position);
        } else {
            this.penetration = A.radius - penetration;
            Vectors2D n = B.normals[faceNormal];
            this.contactCount = 1;
            B.orient.mul(n, normal).negative();
            this.contacts[0].set(a.position.addi(normal.scalar(A.radius)));
        }
    }

    private void polygonVsPolygon() {

    }

    public void penetrationResolution() {

    }

    public void solve() {
        for (int i = 0; i < contactCount; i++) {
            Vectors2D contactA = contacts[i].subtract(A.position);
            Vectors2D contactB = contacts[i].subtract(B.position);

            //Relative velocity created from equation found in GDC talk of box2D lite.
            Vectors2D relativeVel = B.velocity.add(contactB.crossProduct(B.angularVelocity)).subtract(A.velocity).subtract(contactA.crossProduct(A.angularVelocity));

            double contactVel = relativeVel.dotProduct(normal);

            //Prevents objects colliding when they are moving away from each other.
            //If not, objects could still be overlapping after a contact has been resolved and cause objects to stick together
            if (contactVel >= 0) {
                return;
            }

            double acn = contactA.crossProduct(normal);
            double bcn = contactB.crossProduct(normal);
            double inverseMassSum = A.invMass + B.invMass + (acn * acn) * A.invI + (bcn * bcn) * B.invI;

            double j = -(restitution + 1) * contactVel;
            j /= inverseMassSum;

            //Apply contact impulse
            Vectors2D impulse = normal.scalar(j);
            B.velocity = B.velocity.add(impulse.scalar(B.invMass));
            B.angularVelocity += B.invI * contactB.crossProduct(impulse);

            A.velocity = A.velocity.add(impulse.negative().scalar(A.invMass));
            A.angularVelocity += A.invI * contactA.crossProduct(impulse.negative());
        }
    }
}