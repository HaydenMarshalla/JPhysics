package library.joints;

import library.dynamics.Body;
import library.utils.ColourSettings;
import testbed.Camera;
import library.math.Matrix2D;
import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class JointToBody extends Joint {
    private Body object2;
    private Vectors2D offset2;
    private Vectors2D object2AttachmentPoint;

    public JointToBody(Body b1, Body b2, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1, Vectors2D offset2) {
        super(b1, jointLength, jointConstant, dampening, canGoSlack, offset1);
        object2 = b2;
        this.offset2 = offset2;
    }

    @Override
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
        impulse(tension, object1, distance);
        impulse(tension, object2, distance.negativeVec());
    }

    @Override
    public double calculateTension() {
        double distance = object1AttachmentPoint.subtract(object2AttachmentPoint).length();
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
        Vectors2D distance = object2AttachmentPoint.subtract(object1AttachmentPoint);
        distance.normalize();
        Vectors2D relativeVelocity = object2.velocity.subtract(object1.velocity);

        return relativeVelocity.dotProduct(distance);
    }

    @Override
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        Vectors2D obj1Pos = camera.convertToScreen(object1AttachmentPoint);
        Vectors2D obj2Pos = camera.convertToScreen(object2AttachmentPoint);
        g.draw(new Line2D.Double(obj1Pos.x, obj1Pos.y, obj2Pos.x, obj2Pos.y));
    }
}