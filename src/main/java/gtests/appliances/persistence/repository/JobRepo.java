package gtests.appliances.persistence.repository;

import gtests.appliances.persistence.model.EndpointJob;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring data repository for EndpointJob entities
 *
 * @author g-tests
 */
@Repository
public interface JobRepo extends PagingAndSortingRepository<EndpointJob, Long> {

}
