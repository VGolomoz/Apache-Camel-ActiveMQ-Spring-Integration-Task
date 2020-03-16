package app.integration.repositories;

import app.integration.entities.MsgBody;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface extends CrudRepository for generic CRUD operations for MsgBody Entity.
 *
 * @author Vadym
 */
@Repository
public interface MsgBodyRepository extends CrudRepository<MsgBody, Long> {


}
