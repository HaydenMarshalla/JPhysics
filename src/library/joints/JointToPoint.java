package library.joints;

import library.dynamics.Body;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Line2D;

public class JointToPoint extends Joint {
    private Vectors2D pointAttachedTo;

    public JointToPoint(Vectors2D point, Body b1, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1) {
        this(b1, point, jointLength, jointConstant, dampening, canGoSlack, offset1);
    }

    public JointToPoint(Body b1, Vectors2D point, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1) {
        super(b1, jointLength, jointConstant, dampening, canGoSlack, offset1);
        this.pointAttachedTo = point;
    }

    @Override
    public void applyTension() {
        Matrix2D mat1 = new Matrix2D(object1.orientation);
        this.object1AttachmentPoint = object1.position.addi(mat1.mul(offset1, new Vectors2D()));

        double tension = calculateTension();
        Vectors2D distance = pointAttachedTo.subtract(object1AttachmentPoint);
        distance.normalize();

        Vectors2D impulse = distance.scalar(tension);
        object1.applyLinearImpulse(impulse, object1AttachmentPoint.subtract(object1.position));
    }

    @Override
    public double calculateTension() {
        double distance = object1AttachmentPoint.subtract(pointAttachedTo).length();
        if (distance < naturalLength && canGoSlack) {
            return 0;
        }
        double extensionRatio = distance - naturalLength;
        double tensionDueToHooksLaw = extensionRatio * springConstant;
        double tensionDueToMotionDamping = dampeningConstant * rateOfChangeOfExtension();
        return tensionDueToHooksLaw + tensionDueToMotionDamping;
    }

    @Override
    public double rateOfChangeOfExtension() {
        Vectors2D distance = pointAttachedTo.subtract(object1AttachmentPoint);
        distance = distance.normalize();
        Vectors2D relativeVelocity = object1.velocity.negativeVec();

        return relativeVelocity.dotProduct(distance);
    }

    @Override
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.joints);
        Vectors2D obj1Pos = camera.convertToScreen(object1AttachmentPoint);
        Vectors2D obj2Pos = camera.convertToScreen(pointAttachedTo);
        g.draw(new Line2D.Double(obj1Pos.x, obj1Pos.y, obj2Pos.x, obj2Pos.y));
    }
}
