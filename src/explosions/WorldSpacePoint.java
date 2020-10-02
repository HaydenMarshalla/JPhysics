package explosions;

import library.World;
import library.math.Vectors2D;

public abstract class WorldSpacePoint {
    public Vectors2D epicentre;
    public World world;

    public WorldSpacePoint(Vectors2D location, World world) {
        epicentre = location;
        this.world = world;
    }
}