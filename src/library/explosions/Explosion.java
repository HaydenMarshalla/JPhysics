package library.explosions;

import library.dynamics.Body;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.util.ArrayList;

public interface Explosion {
    void applyBlastImpulse(double blastPower);
    void draw(Graphics2D gi, ColourSettings paintSettings, Camera camera);
    void update(ArrayList<Body> bodiesToEvaluate);
    void changeEpicentre(Vectors2D v);
}
