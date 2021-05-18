package library.joints;

import library.dynamics.Body;
import testbed.ColourSettings;
import testbed.Camera;
import library.math.Matrix2D;
import library.math.Vectors2D;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Class for a joint between two bodies.
 */
public class JointToBody extends Joint {
    private Body object2;
    private Vectors2D offset2;
    private Vectors2D object2AttachmentPoint;

    /**
     * Constructor for a joint between two bodies.
     *
     * @param b1            First body the joint is attached to
     * @param b2            Second body the joint is attached to
     * @param jointLength   The desired distance of the joint between two points/bodies
     * @param jointConstant The strength of the joint
     * @param dampening     The dampening constant to use for the joints forces
     * @param canGoSlack    Boolean whether the joint can go slack or not
     * @param offset1       Offset to be applied to the location of the joint relative to b1's object space
     * @param offset2       Offset to be applied to the location of the joint relative to b2's object space
     */
    public JointToBody(Body b1, Body b2, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1, Vectors2D offset2) {
        super(b1, jointLength, jointConstant, dampening, canGoSlack, offset1);
        object2 = b2;
        this.offset2 = offset2;
    }

    /**
     * Applies tension to the two bodies.
     */
    @Override
    public void applyTension() {
        Matrix2D mat1 = new Matrix2D(object1.orientation);
        this.object1AttachmentPoint = object1.position.addi(mat1.mul(offset1, new Vectors2D()));

        Matrix2D mat2 = new Matrix2D(object2.orientation);
        this.object2AttachmentPoint = object2.position.addi(mat2.mul(offset2, new Vectors2D()));

        double tension = calculateTension();
        Vectors2D distance = this.object2AttachmentPoint.subtract(this.object1AttachmentPoint);
        distance.normalize();

        Vectors2D impulse = distance.scalar(tension);
        object1.applyLinearImpulse(impulse, object1AttachmentPoint.subtract(object1.position));
        object2.applyLinearImpulse(impulse.negativeVec(), object2AttachmentPoint.subtract(object2.position));
    }

    /**
     * Calculates tension between the two attachment points of the joints bodies.
     *
     * @return double value of the tension force between the two bodies attachment points
     */
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

    /**
     * Determines the rate of change between two objects.
     *
     * @return double value of the rate of change
     */
    @Override
    public double rateOfChangeOfExtension() {
        Vectors2D distance = object2AttachmentPoint.subtract(object1AttachmentPoint);
        distance.normalize();

        Vectors2D relativeVelocity = object2.velocity.addi(object2AttachmentPoint.subtract(object2.position).crossProduct(object2.angularVelocity)).subtract(object1.velocity).subtract(object1AttachmentPoint.subtract(object1.position).crossProduct(object1.angularVelocity));

        return relativeVelocity.dotProduct(distance);
    }

    /**
     * Implementation of the draw method.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    @Override
    public void draw(Graphics2D g, ColourSettings paintSettings, Camera camera) {
        g.setColor(paintSettings.joints);
        Vectors2D obj1Pos = camera.convertToScreen(object1AttachmentPoint);
        Vectors2D obj2Pos = camera.convertToScreen(object2AttachmentPoint);
        g.draw(new Line2D.Double(obj1Pos.x, obj1Pos.y, obj2Pos.x, obj2Pos.y));
    }
}