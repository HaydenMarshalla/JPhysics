package library.joints;

import library.dynamics.Body;
import library.math.Matrix2D;
import library.math.Vectors2D;
import testbed.ColourSettings;
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
        Matrix2D u = new Matrix2D(object1.orientation);
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

    public abstract void draw(Graphics2D g, ColourSettings paintSettings, Camera camera);
}
