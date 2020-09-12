package raycasts;

import library.Camera;
import library.ColourSettings;
import library.World;
import library.math.Vectors2D;

import java.awt.*;

public abstract class RayCast {
    public Vectors2D epicentre;
    public World world;

    public RayCast(Vectors2D location, World world) {
        epicentre = location;
        this.world = world;
    }

    public abstract void applyBlastImpulse(double blastPower);

    public abstract void draw(Graphics g, ColourSettings paintSettings, Camera camera);
}