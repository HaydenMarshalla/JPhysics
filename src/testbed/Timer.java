package testbed;

/**
 * Timer class.
 */
public class Timer {
    public long prevTime;

    /**
     * No-argument constructor initializes instance variables to current time
     */
    public Timer() {
        reset();
    }

    /**
     * Resets instance values to current time
     */
    public void reset() {
        prevTime = System.nanoTime();
    }

    /**
     * Method to get the time difference between the prevTime and the current time.
     * @return long value with the time difference since prevTime was set
     */
    public long timePassed() {
        return System.nanoTime() - prevTime;
    }
}
