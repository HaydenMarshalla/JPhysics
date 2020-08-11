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
            normal.normalize();
            Vectors2D circleContactPoint = normal.scalar(ca.radius);
            this.contacts[0].set(circleContactPoint.addi(A.position));
        }
    }

    private void circleVsPolygon(Body a, Body b) {
        Circle A = (Circle) a.shape;
        Polygon B = (Polygon) b.shape;

        //Transpose effectively removes the rotation thus allowing the OBB vs OBB detection to become AABB vs OBB
        Vectors2D distOfBodies = a.position.subtract(b.position);
        Vectors2D polyToCircleVec = B.orient.transpose().mul(distOfBodies);
        double penetration = -Double.MAX_VALUE;
        int faceNormalIndex = 0;

        //Applies SAT to check for potential penetration
        //Retrieves best edge of polygon
        for (int i = 0; i < B.vertices.length; i++) {
            Vectors2D v = polyToCircleVec.subtract(B.vertices[i]);
            double distance = B.normals[i].dotProduct(v);

            //If circle is outside of polygon, no collision detected.
            if (distance > A.radius) {
                return;
            }

            if (distance > penetration) {
                faceNormalIndex = i;
                penetration = distance;
            }
        }

        //Get vertex's of best face
        Vectors2D vector1 = B.vertices[faceNormalIndex];
        Vectors2D vector2 = B.vertices[faceNormalIndex + 1 < B.vertices.length ? faceNormalIndex + 1 : 0];

        Vectors2D v1ToV2 = vector2.subtract(vector1);
        Vectors2D circleBodyTov1 = polyToCircleVec.subtract(vector1);
        double firstPolyCorner = circleBodyTov1.dotProduct(v1ToV2);

        Vectors2D v2ToV1 = vector1.subtract(vector2);
        Vectors2D circleBodyTov2 = polyToCircleVec.subtract(vector2);
        double secondPolyCorner = circleBodyTov2.dotProduct(v2ToV1);

        //If first vertex is positive, v1 edge region collision check
        //If second vertex is positive, v2 edge region collision check
        //Else circle has made contact with the polygon face.
        if (firstPolyCorner <= 0.0) {
            penetration = polyToCircleVec.distance(vector1);

            //Check to see if vertex is within the circle
            if (penetration >= A.radius) {
                return;
            }

            this.penetration = penetration;
            contactCount = 1;
            B.orient.mul(this.normal.set(vector1.subtract(polyToCircleVec).normalize()));
            contacts[0] = B.orient.mul(vector1, new Vectors2D()).addi(b.position);
        } else if (secondPolyCorner <= 0.0) {
            penetration = polyToCircleVec.distance(vector2);

            //Check to see if vertex is within the circle
            if (penetration >= A.radius) {
                return;
            }

            this.penetration = penetration;
            contactCount = 1;
            B.orient.mul(this.normal.set(vector2.subtract(polyToCircleVec).normalize()));
            contacts[0] = B.orient.mul(vector2, new Vectors2D()).addi(b.position);

        } else {
            this.penetration = A.radius - penetration;
            Vectors2D faceNormal = B.normals[faceNormalIndex];
            this.contactCount = 1;
            B.orient.mul(faceNormal, this.normal);
            Vectors2D circleContactPoint = a.position.addi(this.normal.negative().scalar(A.radius));
            this.contacts[0].set(circleContactPoint);
        }
    }

    private void polygonVsPolygon() {

    }

    public void penetrationResolution() {

    }

    public void solve() {
        for (int i = 0; i < 1; i++) {
            Vectors2D contactA = contacts[i].subtract(A.position);
            Vectors2D contactB = contacts[i].subtract(B.position);

            //Relative velocity created from equation found in GDC talk of box2D lite.
            Vectors2D relativeVel = B.velocity.addi(contactB.crossProduct(B.angularVelocity)).subtract(A.velocity).subtract(contactA.crossProduct(A.angularVelocity));

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
            B.velocity = B.velocity.addi(impulse.scalar(B.invMass));
            B.angularVelocity += B.invI * contactB.crossProduct(impulse);

            A.velocity = A.velocity.addi(impulse.negative().scalar(A.invMass));
            A.angularVelocity += A.invI * contactA.crossProduct(impulse.negative());
        }
    }
}