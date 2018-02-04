package gtests.appliances.presentation.service;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.validation.JsonValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Representation service for editable state of an endpoint
 *
 * @author g-tests
 */
@Service
public class EditableStateService {

    @Autowired
    private EndpointRepo endpointRepo;

    public Optional<Map<String, Object>> get(String endpointId) {
        return endpointRepo.findOneById(endpointId)
                .map(Endpoint::getEditableState)
                .map(HashMap::new);
    }

    public Optional<Map<String, Object>> update(String endpointId, Map<String, Object> newState) {
        return endpointRepo.findOneById(endpointId)
                .map(endpoint -> {
                    JsonValidationUtil.validateMap(newState, endpoint.getStateSchema());
                    endpoint.setEditableState(newState);
                    endpointRepo.save(endpoint);
                    return new HashMap<>(endpoint.getEditableState());
                });
    }
}
