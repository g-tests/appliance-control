package gtests.appliances.persistence.repository;

import gtests.appliances.persistence.model.EndpointProgram;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring data repository for EndpointProgram entities
 *
 * @author g-tests
 */
@Repository
public interface ProgramRepo extends PagingAndSortingRepository<EndpointProgram, Long> {

}
