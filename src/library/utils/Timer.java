package library.utils;

/**
 * Timer class.
 */
public class Timer {
    public long prevTime;

    /**
     * No-argument constructor initializes instance variables to current time
     */
    public Timer() {
        prevTime = System.nanoTime();
    }

    /**
     * Resets instance values to current time
     */
    public void reset() {
        prevTime = System.nanoTime();
    }

    public long timePassed() {
        return System.nanoTime() - prevTime;
    }
}
