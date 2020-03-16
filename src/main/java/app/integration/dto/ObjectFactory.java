package app.integration.dto;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This class contains factory methods which allows construct new POJO instances for XML content
 */
@XmlRegistry
public final class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * app.integration.dto
     */
    private ObjectFactory() {
    }

    /**
     * Create an instance of {@link Catalog }
     */
    public static Catalog createCatalog() {
        return new Catalog();
    }

    /**
     * Create an instance of {@link CD }
     */
    public static CD createCD() {
        return new CD();
    }

}
