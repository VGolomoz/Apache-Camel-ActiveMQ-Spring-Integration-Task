package app.integration.utility;

import app.integration.utility.interfaces.Incrementable;
import org.springframework.stereotype.Component;

/**
 * Class implements methods of interface Incrementable for undefined file types counter.
 *
 * @author Vadym
 */
@Component
public class UndefinedFileIncrementor implements Incrementable {

    /**
     * Instantiate undefined files counter's value.
     */
    private int undefinedFilesCount = 0;

    /**
     * Increment undefined files counter's value.
     */
    @Override
    public synchronized void incrementValue() {
        undefinedFilesCount++;
    }

    /**
     * Reset undefined files counter's value.
     */
    @Override
    public synchronized void resetValue() {
        undefinedFilesCount = 0;
    }

    /**
     * @return undefined files counter's value.
     */
    @Override
    public synchronized int getValue() {
        return undefinedFilesCount;
    }
}
