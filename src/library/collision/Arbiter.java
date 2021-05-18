package library.collision;


import library.dynamics.Body;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.dynamics.Settings;
import library.math.Vectors2D;

/**
 * Creates manifolds to detect collisions and apply forces to them. Discrete in nature and only evaluates pairs of bodies in a single manifold.
 */
public class Arbiter {
    private final Body A;
    private final Body B;

    /**
     * Getter for Body A.
     *
     * @return Body A
     */
    public Body getA() {
        return A;
    }

    /**
     * Getter for Body B.
     *
     * @return Body B
     */
    public Body getB() {
        return B;
    }

    /**
     * Static fiction constant to be set during the construction of the arbiter.
     */
    double staticFriction;
    /**
     * Dynamic fiction constant to be set during the construction of the arbiter.
     */
    double dynamicFriction;

    /**
     * Main constructor for arbiter that takes two bodies to be evaluated. Sets static and dynamic friction constants here.
     *
     * @param a First body of arbiter.
     * @param b Second body of arbiter.
     */
    public Arbiter(Body a, Body b) {
        this.A = a;
        this.B = b;

        staticFriction = (a.staticFriction + b.staticFriction) / 2;
        dynamicFriction = (a.dynamicFriction + b.dynamicFriction) / 2;
    }

    /**
     * Method to check if point is inside a body in world space.
     *
     * @param b          Body to check against.
     * @param startPoint Vector point to check if its inside the first body.
     * @return boolean value whether the point is inside the first body.
     */
    public static boolean isPointInside(Body b, Vectors2D startPoint) {
        if (b.shape instanceof Polygon) {
            Polygon poly = (Polygon) b.shape;
            for (int i = 0; i < poly.vertices.length; i++) {
                Vectors2D objectPoint = startPoint.subtract(poly.body.position.addi(poly.body.shape.orient.mul(poly.vertices[i], new Vectors2D())));
                if (objectPoint.dotProduct(poly.body.shape.orient.mul(poly.normals[i], new Vectors2D())) > 0) {
                    return false;
                }
            }
        } else if (b.shape instanceof Circle) {
            Circle circle = (Circle) b.shape;
            Vectors2D d = b.position.subtract(startPoint);

            return !(d.length() > circle.radius);
        }

        return true;
    }

    /**
     * Array to save the contact points of the objects body's in world space.
     */
    public final Vectors2D[] contacts = {new Vectors2D(), new Vectors2D()};
    public Vectors2D contactNormal = new Vectors2D();
    public int contactCount = 0;
    public double restitution = 0;

    /**
     * Conducts a narrow phase detection and creates a contact manifold.
     */
    public void narrowPhase() {
        restitution = Math.min(A.restitution, B.restitution);
        if (A.shape instanceof Circle && B.shape instanceof Circle) {
            circleVsCircle();
        } else if (A.shape instanceof Circle && B.shape instanceof Polygon) {
            circleVsPolygon(A, B);
        } else if (A.shape instanceof Polygon && B.shape instanceof Circle) {
            circleVsPolygon(B, A);
            if (this.contactCount > 0) {
                this.contactNormal.negative();
            }
        } else if (A.shape instanceof Polygon && B.shape instanceof Polygon) {
            polygonVsPolygon();
        }
    }

    private double penetration = 0;

    /**
     * Circle vs circle collision detection method
     */
    private void circleVsCircle() {
        Circle ca = (Circle) A.shape;
        Circle cb = (Circle) B.shape;

        Vectors2D normal = B.position.subtract(A.position);

        double distance = normal.length();
        double radius = ca.radius + cb.radius;

        if (distance >= radius) {
            contactCount = 0;
            return;
        }

        this.contactCount = 1;

        if (distance == 0) {
            this.penetration = radius;
            this.contactNormal = new Vectors2D(0, 1);
            this.contacts[0].set(A.position);
        } else {
            this.penetration = radius - distance;
            this.contactNormal = normal.normalize();
            this.contacts[0].set(this.contactNormal.scalar(ca.radius).addi(A.position));
        }
    }

    /**
     * Circle vs Polygon collision detection method
     *
     * @param a Circle object
     * @param b Polygon Object
     */
    private void circleVsPolygon(Body a, Body b) {
        Circle A = (Circle) a.shape;
        Polygon B = (Polygon) b.shape;

        //Transpose effectively removes the rotation thus allowing the OBB vs OBB detection to become AABB vs OBB
        Vectors2D distOfBodies = a.position.subtract(b.position);
        Vectors2D polyToCircleVec = B.orient.transpose().mul(distOfBodies);
        double penetration = -Double.MAX_VALUE;
        int faceNormalIndex = 0;

        //Applies SAT to check for potential penetration
        //Retrieves best face of polygon
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

        //If first vertex is positive, v1 face region collision check
        if (firstPolyCorner <= 0.0) {
            double distBetweenObj = polyToCircleVec.distance(vector1);

            //Check to see if vertex is within the circle
            if (distBetweenObj >= A.radius) {
                return;
            }

            this.penetration = A.radius - distBetweenObj;
            contactCount = 1;
            B.orient.mul(this.contactNormal.set(vector1.subtract(polyToCircleVec).normalize()));
            contacts[0] = B.orient.mul(vector1, new Vectors2D()).addi(b.position);
            return;
        }

        Vectors2D v2ToV1 = vector1.subtract(vector2);
        Vectors2D circleBodyTov2 = polyToCircleVec.subtract(vector2);
        double secondPolyCorner = circleBodyTov2.dotProduct(v2ToV1);

        //If second vertex is positive, v2 face region collision check
        //Else circle has made contact with the polygon face.
        if (secondPolyCorner < 0.0) {
            double distBetweenObj = polyToCircleVec.distance(vector2);

            //Check to see if vertex is within the circle
            if (distBetweenObj >= A.radius) {
                return;
            }

            this.penetration = A.radius - distBetweenObj;
            contactCount = 1;
            B.orient.mul(this.contactNormal.set(vector2.subtract(polyToCircleVec).normalize()));
            contacts[0] = B.orient.mul(vector2, new Vectors2D()).addi(b.position);
        } else {
            double distFromEdgeToCircle = polyToCircleVec.subtract(vector1).dotProduct(B.normals[faceNormalIndex]);

            if (distFromEdgeToCircle >= A.radius) {
                return;
            }

            this.penetration = A.radius - distFromEdgeToCircle;
            this.contactCount = 1;
            B.orient.mul(B.normals[faceNormalIndex], this.contactNormal);
            Vectors2D circleContactPoint = a.position.addi(this.contactNormal.negative().scalar(A.radius));
            this.contacts[0].set(circleContactPoint);
        }
    }

    /**
     * Polygon collision check
     */
    private void polygonVsPolygon() {
        Polygon pa = (Polygon) A.shape;
        Polygon pb = (Polygon) B.shape;

        AxisData aData = new AxisData();
        findAxisOfMinPenetration(aData, pa, pb);
        if (aData.getPenetration() >= 0) {
            return;
        }

        AxisData bData = new AxisData();
        findAxisOfMinPenetration(bData, pb, pa);
        if (bData.getPenetration() >= 0) {
            return;
        }

        int referenceFaceIndex;
        Polygon referencePoly;
        Polygon incidentPoly;
        boolean flip;

        if (selectionBias(aData.getPenetration(), bData.getPenetration())) {
            referencePoly = pa;
            incidentPoly = pb;
            referenceFaceIndex = aData.getReferenceFaceIndex();
            flip = false;
        } else {
            referencePoly = pb;
            incidentPoly = pa;
            referenceFaceIndex = bData.getReferenceFaceIndex();
            flip = true;
        }

        Vectors2D[] incidentFaceVertexes = new Vectors2D[2];
        Vectors2D referenceNormal = referencePoly.normals[referenceFaceIndex];

        //Reference face of reference polygon in object space of incident polygon
        referenceNormal = referencePoly.orient.mul(referenceNormal, new Vectors2D());
        referenceNormal = incidentPoly.orient.transpose().mul(referenceNormal, new Vectors2D());

        //Finds face of incident polygon angled best vs reference poly normal.
        //Best face is the incident face that is the most anti parallel (most negative dot product)
        int incidentIndex = 0;
        double minDot = Double.MAX_VALUE;
        for (int i = 0; i < incidentPoly.vertices.length; i++) {
            double dot = referenceNormal.dotProduct(incidentPoly.normals[i]);

            if (dot < minDot) {
                minDot = dot;
                incidentIndex = i;
            }
        }

        //Incident faces vertexes in world space
        incidentFaceVertexes[0] = incidentPoly.orient.mul(incidentPoly.vertices[incidentIndex], new Vectors2D()).addi(incidentPoly.body.position);
        incidentFaceVertexes[1] = incidentPoly.orient.mul(incidentPoly.vertices[incidentIndex + 1 >= incidentPoly.vertices.length ? 0 : incidentIndex + 1], new Vectors2D()).addi(incidentPoly.body.position);

        //Gets vertex's of reference polygon reference face in world space
        Vectors2D v1 = referencePoly.vertices[referenceFaceIndex];
        Vectors2D v2 = referencePoly.vertices[referenceFaceIndex + 1 == referencePoly.vertices.length ? 0 : referenceFaceIndex + 1];

        //Rotate and translate vertex's of reference poly
        v1 = referencePoly.orient.mul(v1, new Vectors2D()).addi(referencePoly.body.position);
        v2 = referencePoly.orient.mul(v2, new Vectors2D()).addi(referencePoly.body.position);

        Vectors2D refTangent = v2.subtract(v1);
        refTangent.normalize();

        double negSide = -refTangent.dotProduct(v1);
        double posSide = refTangent.dotProduct(v2);
        // Clips the incident face against the reference
        int np = clip(refTangent.negativeVec(), negSide, incidentFaceVertexes);

        if (np < 2) {
            return;
        }

        np = clip(refTangent, posSide, incidentFaceVertexes);

        if (np < 2) {
            return;
        }

        Vectors2D refFaceNormal = refTangent.normal().negativeVec();

        Vectors2D[] contactVectorsFound = new Vectors2D[2];
        double totalPen = 0;
        int contactsFound = 0;

        //Discards points that are positive/above the reference face
        for (int i = 0; i < 2; i++) {
            double separation = refFaceNormal.dotProduct(incidentFaceVertexes[i]) - refFaceNormal.dotProduct(v1);
            if (separation <= 0.0 + Settings.EPSILON) {
                contactVectorsFound[contactsFound] = incidentFaceVertexes[i];
                totalPen += -separation;
                contactsFound++;
            }

        }

        Vectors2D contactPoint;
        if (contactsFound == 1) {
            contactPoint = contactVectorsFound[0];
            this.penetration = totalPen;
        } else {
            contactPoint = (contactVectorsFound[1].addi(contactVectorsFound[0])).scalar(0.5);
            this.penetration = totalPen / 2;
        }
        this.contactCount = 1;
        this.contacts[0].set(contactPoint);
        contactNormal.set(flip ? refFaceNormal.negative() : refFaceNormal);
    }

    /**
     * Clipping for polygon collisions. Clips incident face against side planes of the reference face.
     *
     * @param planeTangent Plane to clip against
     * @param offset       Offset for clipping in world space to incident face.
     * @param incidentFace Clipped face vertex's
     * @return Number of clipped vertex's
     */
    private int clip(Vectors2D planeTangent, double offset, Vectors2D[] incidentFace) {
        int num = 0;
        Vectors2D[] out = {
                new Vectors2D(incidentFace[0]),
                new Vectors2D(incidentFace[1])
        };
        double dist = planeTangent.dotProduct(incidentFace[0]) - offset;
        double dist1 = planeTangent.dotProduct(incidentFace[1]) - offset;

        if (dist <= 0.0) out[num++].set(incidentFace[0]);
        if (dist1 <= 0.0) out[num++].set(incidentFace[1]);

        if (dist * dist1 < 0.0) {
            double interp = dist / (dist - dist1);

            out[num].set(incidentFace[1].subtract(incidentFace[0]).scalar(interp).addi(incidentFace[0]));
            num++;
        }

        incidentFace[0] = out[0];
        incidentFace[1] = out[1];

        return num;
    }

    /**
     * Finds the incident face of polygon A in object space relative to polygons B position.
     *
     * @param data Data obtained from earlier penetration test.
     * @param A    Polygon A to test.
     * @param B    Polygon B to test.
     */
    public void findAxisOfMinPenetration(AxisData data, Polygon A, Polygon B) {
        double distance = -Double.MAX_VALUE;
        int bestIndex = 0;

        for (int i = 0; i < A.vertices.length; i++) {
            //Applies polygon A's orientation to its normals for calculation.
            Vectors2D polyANormal = A.orient.mul(A.normals[i], new Vectors2D());

            //Rotates the normal by the clock wise rotation matrix of B to put the normal relative to the object space of polygon B
            //Polygon b is axis aligned and the normal is located according to this in the correct position in object space
            Vectors2D objectPolyANormal = B.orient.transpose().mul(polyANormal, new Vectors2D());

            double bestProjection = Double.MAX_VALUE;
            Vectors2D bestVertex = B.vertices[0];

            //Finds the index of the most negative vertex relative to the normal of polygon A
            for (int x = 0; x < B.vertices.length; x++) {
                Vectors2D vertex = B.vertices[x];
                double projection = vertex.dotProduct(objectPolyANormal);

                if (projection < bestProjection) {
                    bestVertex = vertex;
                    bestProjection = projection;
                }
            }

            //Distance of B to A in world space space
            Vectors2D distanceOfBA = A.body.position.subtract(B.body.position);

            //Best vertex relative to polygon B in object space
            Vectors2D polyANormalVertex = B.orient.transpose().mul(A.orient.mul(A.vertices[i], new Vectors2D()).addi(distanceOfBA));

            //Distance between best vertex and polygon A's plane in object space
            double d = objectPolyANormal.dotProduct(bestVertex.subtract(polyANormalVertex));

            //Records penetration and vertex
            if (d > distance) {
                distance = d;
                bestIndex = i;
            }
        }
        data.setPenetration(distance);
        data.setReferenceFaceIndex(bestIndex);
    }

    /**
     * Resolves any penetrations that are left overlapping between shapes. This can be cause due to integration errors of the solvers integration method.
     * Based on linear projection to move the shapes away from each other based on a correction constant and scaled relative to the inverse mass of the objects.
     */
    public void penetrationResolution() {
        double penetrationTolerance = penetration - Settings.PENETRATION_ALLOWANCE;

        if (penetrationTolerance <= 0.0) {
            return;
        }

        double totalMass = A.mass + B.mass;
        double correction = (penetrationTolerance * Settings.PENETRATION_CORRECTION) / totalMass;
        A.position = A.position.addi(contactNormal.scalar(-A.mass * correction));
        B.position = B.position.addi(contactNormal.scalar(B.mass * correction));
    }

    /**
     * Solves the current contact manifold and applies impulses based on any contacts found.
     */
    public void solve() {
        Vectors2D contactA = contacts[0].subtract(A.position);
        Vectors2D contactB = contacts[0].subtract(B.position);

        //Relative velocity created from equation found in GDC talk of box2D lite.
        Vectors2D relativeVel = B.velocity.addi(contactB.crossProduct(B.angularVelocity)).subtract(A.velocity).subtract(contactA.crossProduct(A.angularVelocity));

        //Positive = converging Negative = diverging
        double contactVel = relativeVel.dotProduct(contactNormal);

        //Prevents objects colliding when they are moving away from each other.
        //If not, objects could still be overlapping after a contact has been resolved and cause objects to stick together
        if (contactVel >= 0) {
            return;
        }

        double acn = contactA.crossProduct(contactNormal);
        double bcn = contactB.crossProduct(contactNormal);
        double inverseMassSum = A.invMass + B.invMass + (acn * acn) * A.invI + (bcn * bcn) * B.invI;

        double j = -(restitution + 1) * contactVel;
        j /= inverseMassSum;

        Vectors2D impulse = contactNormal.scalar(j);
        B.applyLinearImpulse(impulse, contactB);
        A.applyLinearImpulse(impulse.negativeVec(), contactA);

        relativeVel = B.velocity.addi(contactB.crossProduct(B.angularVelocity)).subtract(A.velocity).subtract(contactA.crossProduct(A.angularVelocity));

        Vectors2D t = relativeVel.copy();
        t.add(contactNormal.scalar(-relativeVel.dotProduct(contactNormal))).normalize();

        double jt = -relativeVel.dotProduct(t);
        jt /= inverseMassSum;

        Vectors2D tangentImpulse;
        if (StrictMath.abs(jt) < j * staticFriction) {
            tangentImpulse = t.scalar(jt);
        } else {
            tangentImpulse = t.scalar(j).scalar(-dynamicFriction);
        }

        B.applyLinearImpulse(tangentImpulse, contactB);
        A.applyLinearImpulse(tangentImpulse.negativeVec(), contactA);
    }

    /**
     * Selects one value over another. Intended for polygon collisions to aid in choosing which axis of separation intersects the other in a consistent manner.
     * Floating point error can occur in the rotation calculations thus this method helps with choosing one axis over another in a consistent manner for stability.
     *
     * @param a penetration value a
     * @param b penetration value b
     * @return boolean value whether a is to be preferred or not.
     */
    private static boolean selectionBias(double a, double b) {
        return a >= b * Settings.BIAS_RELATIVE + a * Settings.BIAS_ABSOLUTE;
    }
}

/**
 * Class for data related to axis
 */
class AxisData {
    private double penetration;
    private int referenceFaceIndex;

    /**
     * Default constructor
     */
    AxisData() {
        penetration = -Double.MAX_VALUE;
        referenceFaceIndex = 0;
    }

    /**
     * Sets penetration value.
     *
     * @param value Penetration value of type double.
     */
    public void setPenetration(double value) {
        penetration = value;
    }

    /**
     * Sets the reference face index variable to an int value.
     *
     * @param value Value to set index variable to.
     */
    public void setReferenceFaceIndex(int value) {
        referenceFaceIndex = value;
    }

    /**
     * Gets the penetration value stored.
     *
     * @return double penetration value.
     */
    public double getPenetration() {
        return penetration;
    }

    /**
     * Gets the referenceFaceIndex value stored.
     *
     * @return int referenceFaceIndex value.
     */
    public int getReferenceFaceIndex() {
        return referenceFaceIndex;
    }
}