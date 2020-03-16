package app.integration.repositories;
import app.integration.entities.MsgHeaders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface extends CrudRepository for generic CRUD operations for MsgHeaders Entity.
 *
 * @author Vadym
 */
@Repository
public interface MsgHeadersRepository extends CrudRepository<MsgHeaders, Long> {
}
