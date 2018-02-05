package gtests.appliances.persistence.repository;

import gtests.appliances.persistence.model.EndpointJob;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "select job from EndpointJob job where job.endpoint.id = :endpointId and job.finished is null")
    List<EndpointJob> findPendingByEndpointId(@Param("endpointId") String endpointId);
}
