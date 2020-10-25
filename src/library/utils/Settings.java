package library.utils;

import java.util.Random;

public class Settings {
    public static final double PENETRATION_ALLOWANCE = 0.2;
    public static final double PENETRATION_CORRECTION = 0.2;
    public static final double BIAS_RELATIVE = 0.95;
    public static final double BIAS_ABSOLUTE = 0.01;

    public static final double HERTZ = 120000.0;
    public static final double FPS = 60.0;
    public static final int ITERATIONS = 100;
    public static final double EPSILON = 1E-12;

    public static double generateRandomNoInRange(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    public static int generateRandomNoInRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}