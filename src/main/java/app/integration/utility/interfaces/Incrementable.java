package app.integration.utility.interfaces;

/**
 * Interface which provides basic operations for counters.
 *
 * @author Vadym
 */
public interface Incrementable {
    /**
     * Increment counter's value.
     */
    void incrementValue();

    /**
     * Reset counter's value.
     */
    void resetValue();

    /**
     * @return counter's value.
     */
    int getValue();
}
