package app.integration.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * POJO class to construct new instances for XML content
 *
 * @author Vadym
 */
@Data
@XmlRootElement(name = "CATALOG")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryClass= ObjectFactory.class, factoryMethod="createCatalog")
public class Catalog {

    /**
     * List for adding nested objects.
     */
    @XmlElement(name = "CD")
    private List<CD> cdList;

}
