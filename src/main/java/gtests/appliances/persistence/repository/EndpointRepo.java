package gtests.appliances.persistence.repository;

import gtests.appliances.persistence.model.Endpoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data repository for Endpoint entities
 *
 * @author g-tests
 */
@Repository
public interface EndpointRepo extends PagingAndSortingRepository<Endpoint, String> {

    @Override
    List<Endpoint> findAll();

}
