package library.joints;

import library.dynamics.Body;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.ColourSettings;
import testbed.Camera;

import java.awt.*;

/**
 * Abstract class for joints holding all the common properties of joints.
 */
public abstract class Joint {
    protected final Body object1;
    protected final double naturalLength;
    protected final double springConstant;
    protected final double dampeningConstant;
    protected final boolean canGoSlack;
    protected final Vectors2D offset1;
    protected Vectors2D object1AttachmentPoint;

    /**
     * Default constructor
     *
     * @param b1            A body the joint is attached to
     * @param jointLength   The desired distance of the joint between two points/bodies
     * @param jointConstant The strength of the joint
     * @param dampening     The dampening constant to use for the joints forces
     * @param canGoSlack    Boolean whether the joint can go slack or not
     * @param offset1       Offset to be applied to the location of the joint relative to b1's object space.
     */
    protected Joint(Body b1, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1) {
        object1 = b1;
        Matrix2D u = new Matrix2D(object1.orientation);
        this.object1AttachmentPoint = object1.position.addi(u.mul(offset1, new Vectors2D()));
        this.naturalLength = jointLength;
        this.springConstant = jointConstant;
        this.dampeningConstant = dampening;
        this.canGoSlack = canGoSlack;
        this.offset1 = offset1;
    }

    /**
     * Abstract method to apply tension to the joint
     */
    public abstract void applyTension();

    /**
     * Abstract method to calculate tension between the joint
     *
     * @return double value of the tension force between two points/bodies
     */
    public abstract double calculateTension();

    /**
     * Determines the rate of change between two objects/points.
     * @return double value of the rate of change
     */
    public abstract double rateOfChangeOfExtension();

    /**
     * Abstract draw method using graphics2D from java.swing for debug drawer.
     *
     * @param g             Graphics2D object to draw to
     * @param paintSettings Colour settings to draw the objects to screen with
     * @param camera        Camera class used to convert points from world space to view space
     */
    public abstract void draw(Graphics2D g, ColourSettings paintSettings, Camera camera);
}