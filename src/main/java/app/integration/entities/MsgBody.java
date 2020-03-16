package app.integration.entities;

import lombok.Data;
import javax.persistence.*;

/**
 * POJO Class to save body a processed message in the database
 *
 * @author Vadym
 */
@Entity
@Data
@Table(name = "body")
public class MsgBody {

    /**
     * Primary key of an entity in database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Message body's value.
     */
    @Lob
    private String value;

    /**
     * One-to-one mapping between MsgBody and MsgHeaders by foreign key.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "headers_id", referencedColumnName = "id")
    private MsgHeaders msgHeaders;

}
