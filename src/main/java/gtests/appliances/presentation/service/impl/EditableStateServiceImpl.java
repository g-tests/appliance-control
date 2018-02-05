package gtests.appliances.presentation.service.impl;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.presentation.service.EditableStateService;
import gtests.appliances.validation.JsonValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * EditableStateService implementation
 *
 * @author g-tests
 */
@Service
public class EditableStateServiceImpl implements EditableStateService {

    @Autowired
    private EndpointRepo endpointRepo;

    @Override
    public Optional<Map<String, Object>> get(String endpointId) {
        return endpointRepo.findOneById(endpointId)
                .map(Endpoint::getEditableState)
                .map(HashMap::new);
    }

    @Override
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
