package app.integration.utility;

import app.integration.utility.interfaces.Incrementable;
import org.springframework.stereotype.Component;

/**
 * Class implements methods of interface Incrementable for text file counter.
 *
 * @author Vadym
 */
@Component
public class TxtFileIncrementor implements Incrementable {

    /**
     * Instantiate text files counter's value.
     */
    private int txtFilesCount = 0;

    /**
     * Increment text files counter's value.
     */
    @Override
    public synchronized void incrementValue() {
        txtFilesCount++;
    }

    /**
     * Reset text files counter's value.
     */
    @Override
    public synchronized void resetValue() {
        txtFilesCount = 0;
    }

    /**
     * @return text files counter's value.
     */
    @Override
    public synchronized int getValue() {
        return txtFilesCount;
    }
}
