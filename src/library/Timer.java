package library;

/**
 * Timer class.
 */
public class Timer {
    public long prevTime;
    public long newTime;

    /**
     * No-argument constructor initializes instance variables to current time
     */
    public Timer() {
        prevTime = newTime = System.nanoTime();
    }

    /**
     * Resets instance values to current time
     */
    public void reset() {
        prevTime = newTime = System.nanoTime();
    }

    /**
     * Updates the new time variable to current time
     */
    public void updateNewTime() {
        newTime = System.nanoTime();
    }
}
