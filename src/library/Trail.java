package library;

import library.math.Vectors2D;

public class Trail {
    private final Vectors2D[] trailPoints;
    private final int arrayEndPos;
    private final int skipInterval;
    private int counter;

    public Trail(int noOfTrailPoints, int skipInterval) {
        trailPoints = Vectors2D.createArray(noOfTrailPoints);
        arrayEndPos = noOfTrailPoints - 1;
        this.skipInterval = skipInterval;
        counter = 0;
    }

    private int trailEndPointIndex = 0;

    public void updateTrail(Body b) {
        if (counter >= skipInterval) {
            if (trailEndPointIndex <= arrayEndPos) {
                trailPoints[trailEndPointIndex] = b.position.copy();
                trailEndPointIndex++;
            } else {
                System.arraycopy(trailPoints, 1, trailPoints, 0, arrayEndPos);
                trailPoints[arrayEndPos] = b.position.copy();
            }
            counter = 0;
        } else {
            counter++;
        }
    }

    public Vectors2D[] getTrailPoints() {
        return trailPoints;
    }
}