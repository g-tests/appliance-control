package gtests.appliances.repository;

import gtests.appliances.model.Endpoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring data repository for Endpoint entities
 *
 * @author g-tests
 */
@Repository
public interface EndpointRepo extends PagingAndSortingRepository<Endpoint, String> {

}
