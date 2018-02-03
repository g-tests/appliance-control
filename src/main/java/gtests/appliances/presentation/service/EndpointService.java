package gtests.appliances.presentation.service;

import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.presentation.view.EndpointMainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static gtests.appliances.presentation.mapper.EndpointMapper.ENDPOINT_MAPPER;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * Representation service for endpoint resource
 *
 * @author g-tests
 */
@Service
public class EndpointService {

    private EndpointRepo endpointRepo;

    @Autowired
    public EndpointService(EndpointRepo endpointRepo) {
        this.endpointRepo = endpointRepo;
    }

    /**
     * Finds endpoint by id and converts it to main representation
     *
     * @param id endpoint id
     * @return Optional view of endpoint (if found)
     */
    public Optional<EndpointMainView> get(String id) {
        return ofNullable(endpointRepo.findOne(id))
                .map(ENDPOINT_MAPPER::entityToView);
    }

    /**
     * Finds all endpoints and converts them to main representation
     *
     * @return list of views
     */
    public List<EndpointMainView> list() {
        return endpointRepo.findAll()
                .stream()
                .map(ENDPOINT_MAPPER::entityToView)
                .collect(toList());
    }
}
