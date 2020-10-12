package library.joints;

import library.dynamics.Body;
import library.math.Matrix2D;
import library.math.Vectors2D;
import library.utils.ColourSettings;
import testbed.Camera;

import java.awt.*;

public abstract class Joint {
    protected final Body object1;
    protected final double naturalLength;
    protected final double springConstant;
    protected final double dampeningConstant;
    protected final boolean canGoSlack;
    protected final Vectors2D offset1;
    protected Vectors2D object1AttachmentPoint;

    protected Joint(Body b1, double jointLength, double jointConstant, double dampening, boolean canGoSlack, Vectors2D offset1) {
        object1 = b1;
        Matrix2D u = new Matrix2D();
        u.set(object1.orientation);
        this.object1AttachmentPoint = object1.position.addi(u.mul(offset1, new Vectors2D()));
        this.naturalLength = jointLength;
        this.springConstant = jointConstant;
        this.dampeningConstant = dampening;
        this.canGoSlack = canGoSlack;
        this.offset1 = offset1;
    }

    public abstract void applyTension();

    public abstract double calculateTension();

    public abstract double rateOfChangeOfExtension();

    public void impulse(double force, Body B, Vectors2D tangent) {
        Vectors2D impulse = tangent.scalar(force);
        B.velocity = B.velocity.addi(impulse.scalar(B.invMass));
        B.angularVelocity += B.invI * object1AttachmentPoint.subtract(object1.position).crossProduct(impulse);
    }

    public abstract void draw(Graphics2D g, ColourSettings paintSettings, Camera camera);
}
