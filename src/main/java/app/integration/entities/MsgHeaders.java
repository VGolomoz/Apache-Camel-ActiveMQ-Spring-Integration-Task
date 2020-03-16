package app.integration.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

/**
 * POJO Class to save headers and entry time a processed message in the database
 *
 * @author Vadym
 */
@Entity
@Data
@Table(name = "headers")
public class MsgHeaders {

    /**
     * Primary key of an entity in database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Message headers value.
     */
    @Lob
    private String value;

    /**
     * Logging entry time.
     */
    private Calendar entryTime;

    /**
     * One-to-one mapping between MsgBody and MsgHeaders by foreign key.
     */
    @OneToOne(mappedBy = "msgHeaders")
    private MsgBody msgBody;

}
