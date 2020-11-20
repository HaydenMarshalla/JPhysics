package library.explosions;

import library.dynamics.Body;
import library.dynamics.Ray;
import library.dynamics.RayInformation;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.ColourSettings;

import java.awt.*;
import java.util.ArrayList;

public class RaycastExplosion implements Explosion {
    private final RayScatter rayScatter;

    public RaycastExplosion(Vectors2D epicentre, int noOfRays, int distance, ArrayList<Body> worldBodies) {
        rayScatter = new RayScatter(epicentre, noOfRays);
        rayScatter.castRays(distance);
        update(worldBodies);
    }

    @Override
    public void changeEpicentre(Vectors2D v) {
        rayScatter.changeEpicentre(v);
    }

    public ArrayList<RayInformation> raysInContact = new ArrayList<>();

    @Override
    public void update(ArrayList<Body> worldBodies) {
        raysInContact.clear();
        rayScatter.updateRays(worldBodies);
        Ray[] rayArray = rayScatter.getRays();
        for (Ray ray : rayArray) {
            RayInformation rayInfo = ray.getRayInformation();
            if (rayInfo != null) {
                raysInContact.add(rayInfo);
            }
        }
    }

    @Override
    public void applyBlastImpulse(double blastPower) {
        for (RayInformation ray : raysInContact) {
            Vectors2D blastDir = ray.getCoord().subtract(rayScatter.getEpicentre());
            double distance = blastDir.length();
            if (distance == 0) return;

            double invDistance = 1 / distance;
            Vectors2D impulseMag = blastDir.normalize().scalar(blastPower * invDistance);
            Body b = ray.getB();
            b.applyLinearImpulse(impulseMag, ray.getCoord().subtract(b.position));
        }
    }

    @Override
    public void draw(Graphics2D g2d, ColourSettings paintSettings, Camera camera) {
        rayScatter.draw(g2d, paintSettings, camera);
    }
}

