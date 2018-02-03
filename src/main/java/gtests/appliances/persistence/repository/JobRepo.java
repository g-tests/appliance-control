package gtests.appliances.persistence.repository;

import gtests.appliances.persistence.model.EndpointJob;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data repository for EndpointJob entities
 *
 * @author g-tests
 */
@Repository
public interface JobRepo extends PagingAndSortingRepository<EndpointJob, Long> {

    List<EndpointJob> findByEndpointId(String endpointId);

    EndpointJob findOneByIdAndEndpointId(Long jobId, String endpointId);

    long deleteOneByIdAndEndpointId(Long jobId, String endpointId);
}
