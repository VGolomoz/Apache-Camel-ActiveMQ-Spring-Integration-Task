package app.integration.utility;

import app.integration.utility.interfaces.Incrementable;
import org.springframework.stereotype.Component;

/**
 * Class implements methods of interface Incrementable for xml files counter.
 *
 * @author Vadym
 */
@Component
public class XmlFileIncrementor implements Incrementable {

    /**
     * Instantiate xml files counter's value.
     */
    private int xmlFilesCount = 0;

    /**
     * Increment xml files counter's value.
     */
    @Override
    public synchronized void incrementValue() {
        xmlFilesCount++;
    }

    /**
     * Reset xml files counter's value.
     */
    @Override
    public synchronized void resetValue() {
        xmlFilesCount = 0;
    }

    /**
     * @return xml files counter's value.
     */
    @Override
    public synchronized int getValue() {
        return xmlFilesCount;
    }
}
