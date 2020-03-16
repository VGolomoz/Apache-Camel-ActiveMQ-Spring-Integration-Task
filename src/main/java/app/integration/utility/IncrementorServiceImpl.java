package app.integration.utility;

import app.integration.utility.interfaces.Incrementable;
import app.integration.utility.interfaces.IncrementorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class implements methods of interface IncrementorService which union and provides operations with all counters types.
 *
 * @author Vadym
 */
@Component
public class IncrementorServiceImpl implements IncrementorService {

    /**
     * Instantiate total files counter.
     */
    private int totalFilesCount = 0;
    /**
     * Instantiate TxtFileIncrementor bean
     */
    private Incrementable txtIncrementor;
    /**
     * Instantiate XmlFileIncrementor bean
     */
    private Incrementable xmlIncrementor;
    /**
     * Instantiate UndefinedFileIncrementor bean
     */
    private Incrementable undefinedIncrementor;

    /**
     * Constructs a new instance of class and fill required dependencies
     */
    @Autowired
    public IncrementorServiceImpl(@Qualifier("txtFileIncrementor") Incrementable txtIncrementor,
                                  @Qualifier("xmlFileIncrementor") Incrementable xmlIncrementor,
                                  @Qualifier("undefinedFileIncrementor") Incrementable undefinedIncrementor) {
        this.txtIncrementor = txtIncrementor;
        this.xmlIncrementor = xmlIncrementor;
        this.undefinedIncrementor = undefinedIncrementor;
    }

    /**
     * Reset text, xml, undefined and total counters values.
     */
    @Override
    public synchronized void resetCounters() {
        txtIncrementor.resetValue();
        xmlIncrementor.resetValue();
        undefinedIncrementor.resetValue();
        totalFilesCount = 0;
    }

    /**
     * Increment text files counter and total counter values.
     */
    @Override
    public synchronized void incrementTxtCount() {
        txtIncrementor.incrementValue();
        totalFilesCount++;
    }
    /**
     * Increment xml files counter and total counter values.
     */
    @Override
    public synchronized void incrementXmlCount() {
        xmlIncrementor.incrementValue();
        totalFilesCount++;
    }

    /**
     * Increment undefined files counter and total counter values.
     */
    @Override
    public synchronized void incrementUndefinedCount() {
        undefinedIncrementor.incrementValue();
        totalFilesCount++;
    }

    /**
     * @return total files counter's value.
     */
    @Override
    public synchronized int getTotalCounterValue() {
        return totalFilesCount;
    }

    /**
     * @return report about values for text, xml and undefined counters.
     */
    @Override
    public synchronized String getCountersValues() {
        return "Was processed: txtFiles = " + txtIncrementor.getValue() + ", xmlFiles = " + xmlIncrementor.getValue() +
                ", undefinedFiles = " + undefinedIncrementor.getValue();
    }
}
