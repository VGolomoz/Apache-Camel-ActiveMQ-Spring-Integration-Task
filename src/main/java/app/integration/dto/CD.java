package app.integration.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * POJO class to construct new instances for XML content
 *
 * @author Vadym
 */
@Data
@XmlRootElement(name = "CD")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryClass= ObjectFactory.class, factoryMethod="createCD")
public class CD {

    @XmlElement(name = "TITLE")
    private String title;
    @XmlElement(name = "ARTIST")
    private String artist;
    @XmlElement(name = "COUNTRY")
    private String country;
    @XmlElement(name = "COMPANY")
    private String company;
    @XmlElement(name = "PRICE")
    private String price;
    @XmlElement(name = "YEAR")
    private String year;

}
