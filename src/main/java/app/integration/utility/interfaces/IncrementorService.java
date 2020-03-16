package app.integration.utility.interfaces;

/**
 * Interface which union and provides operations with all counters types.
 *
 * @author Vadym
 */
public interface IncrementorService {

    /**
     * Increment text files counter's value.
     */
    void incrementTxtCount();

    /**
     * Increment xml files counter's value.
     */
    void incrementXmlCount();

    /**
     * Increment undefined files counter's value.
     */
    void incrementUndefinedCount();

    /**
     * Reset all counters values.
     */
    void resetCounters();

    /**
     * @return total files counter's value.
     */
    int getTotalCounterValue();

    /**
     * @return report about values for each specific counter.
     */
    String getCountersValues();

}
