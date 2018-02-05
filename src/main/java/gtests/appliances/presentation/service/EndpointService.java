package gtests.appliances.presentation.service;

import gtests.appliances.presentation.view.EndpointMainView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Representation service for endpoint resource
 *
 * @author g-tests
 */
public interface EndpointService {

    @Transactional(readOnly = true)
    Optional<EndpointMainView> get(String id);

    @Transactional(readOnly = true)
    List<EndpointMainView> list();
}
