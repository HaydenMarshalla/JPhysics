package library.dynamics;

import java.util.Random;

/**
 * Settings class where all the constants are stored for the physics engine.
 */
public class Settings {
    public static final double PENETRATION_ALLOWANCE = 0.01;
    public static final double PENETRATION_CORRECTION = 0.5;
    public static final double BIAS_RELATIVE = 0.95;
    public static final double BIAS_ABSOLUTE = 0.01;

    public static double HERTZ = 60.0;
    public static final int ITERATIONS = 100;
    public static final double EPSILON = 1E-12;

    /**
     * Generates a random number within the desired range.
     * @param min Minimum double value that the range can fall inside
     * @param max Maximum double value that the range can fall inside
     * @return double value inside the range of min and max supplied
     */
    public static double generateRandomNoInRange(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    /**
     * Generates a random number within the desired range.
     * @param min Minimum int value that the range can fall inside
     * @param max Maximum int value that the range can fall inside
     * @return int value inside the range of min and max supplied
     */
    public static int generateRandomNoInRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}