package library.joints;

import library.dynamics.Body;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.ColourSettings;
import testbed.Camera;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Class for a joint between a body and a point in world space.
 */
public class JointToPoint extends Joint {
    private final Vectors2D pointAttachedTo;

    /**
     * Constructor for a joint between a body and a point.
     *
     * @param point         The point the joint is attached to
     * @param b1            First body the joint is attached to
     * @param jointLength   The desired distance of the joint between two points/bodies
     * @param jointConstant The strength of the joint
     * @param dampening     The dampening constant to use for the joints forces
     * @param canGoSlack    Boolean whether the joint can go slack or not
     * @param offset1       Offset to be applied to the location of the joint relative to b1's object space
     */
    public JointToPoint(Vectors2D point, Body b1, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1) {
        this(b1, point, jointLength, jointConstant, dampening, canGoSlack, offset1);
    }

    /**
     * Convenience constructor that works like
     * {@link #JointToPoint(Vectors2D, Body, double, double, double, boolean, Vectors2D)}
     *
     * @param point         The point the joint is attached to
     * @param b1            First body the joint is attached to
     * @param jointLength   The desired distance of the joint between two points/bodies
     * @param jointConstant The strength of the joint
     * @param dampening     The dampening constant to use for the joints forces
     * @param canGoSlack    Boolean whether the joint can go slack or not
     * @param offset1       Offset to be applied to the location of the joint relative to b1's object space
     */
    public JointToPoint(Body b1, Vectors2D point, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1) {
        super(b1, jointLength, jointConstant, dampening, canGoSlack, offset1);
        this.pointAttachedTo = point;
    }

    /**
     * Applies tension to the body attached to the joint.
     */
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

    /**
     * Calculates tension between the two attachment points of the joints body and point.
     *
     * @return double value of the tension force between the point and attached bodies point
     */
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

    /**
     * Determines the rate of change between the attached point and body.
     *
     * @return double value of the rate of change
     */
    @Override
    public double rateOfChangeOfExtension() {
        Vectors2D distance = pointAttachedTo.subtract(object1AttachmentPoint);
        distance.normalize();
        Vectors2D relativeVelocity = object1.velocity.negativeVec().subtract(object1AttachmentPoint.subtract(object1.position).crossProduct(object1.angularVelocity));

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
        Vectors2D obj2Pos = camera.convertToScreen(pointAttachedTo);
        g.draw(new Line2D.Double(obj1Pos.x, obj1Pos.y, obj2Pos.x, obj2Pos.y));
    }
}
