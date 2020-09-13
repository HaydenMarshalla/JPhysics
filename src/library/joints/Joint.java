package library.joints;

import library.Body;
import library.Camera;
import library.math.Matrix2D;
import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class Joint {
    private final Body object1;
    private final Body object2;
    private final double naturalLength;
    private final double springConstant;
    private final double dampeningConstant;
    private final boolean canGoSlack;
    private final Vectors2D offset1;
    private final Vectors2D offset2;

    private Vectors2D object1AttachmentPoint;
    private Vectors2D object2AttachmentPoint;

    public Joint(Body b1, Body b2, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1, Vectors2D offset2) {
        object1 = b1;
        object2 = b2;
        Matrix2D u = new Matrix2D();
        u.set(object1.orientation);
        this.object1AttachmentPoint = object1.position.addi(u.mul(offset1, new Vectors2D()));
        u.set(object2.orientation);
        this.object2AttachmentPoint = object2.position.addi(u.mul(offset2, new Vectors2D()));
        this.naturalLength = jointLength;
        this.springConstant = jointConstant;
        this.dampeningConstant = dampening;
        this.canGoSlack = canGoSlack;
        this.offset1 = offset1;
        this.offset2 = offset2;
    }

    public void applyTension() {
        Matrix2D mat1 = new Matrix2D();
        mat1.set(object1.orientation);
        this.object1AttachmentPoint = object1.position.addi(mat1.mul(offset1, new Vectors2D()));

        Matrix2D mat2 = new Matrix2D();
        mat2.set(object2.orientation);
        this.object2AttachmentPoint = object2.position.addi(mat2.mul(offset2, new Vectors2D()));

        double tension = calculateTension();
        Vectors2D distance = this.object2AttachmentPoint.subtract(this.object1AttachmentPoint);
        distance.normalize();
        impulse(tension, object1, object2, distance);
    }

    public double calculateTension() {
        double distance;
        distance = object1AttachmentPoint.subtract(object2AttachmentPoint).length();
        if (distance < naturalLength && canGoSlack) {
            return 0;
        }
        double extensionRatio = distance - naturalLength;
        double tensionDueToHooksLaw = extensionRatio * springConstant;
        double tensionDueToMotionDamping = dampeningConstant * rateOfChangeOfExtension();
        return tensionDueToHooksLaw + tensionDueToMotionDamping;
    }

    public double rateOfChangeOfExtension() {
        Vectors2D distance = object2AttachmentPoint.subtract(object1AttachmentPoint);
        distance.normalize();
        Vectors2D relativeVelocity = object2.velocity.subtract(object1.velocity);

        return relativeVelocity.dotProduct(distance);
    }

    private void impulse(double force, Body B, Body A, Vectors2D tangent) {
        Vectors2D impulse = tangent.scalar(force);

        B.velocity = B.velocity.addi(impulse.scalar(B.invMass));
        B.angularVelocity += B.invI * object1AttachmentPoint.subtract(object1.position).crossProduct(impulse);

        A.velocity = A.velocity.subtract(impulse.scalar(A.invMass));
        A.angularVelocity -= A.invI * object2AttachmentPoint.subtract(object2.position).crossProduct(impulse);
    }

    public void draw(Graphics2D g, Camera camera) {
        Vectors2D obj1Pos = camera.scaleToScreen(object1AttachmentPoint);
        Vectors2D obj2Pos = camera.scaleToScreen(object2AttachmentPoint);
        g.draw(new Line2D.Double(obj1Pos.x, obj1Pos.y, obj2Pos.x, obj2Pos.y));
    }
}