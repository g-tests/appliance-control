package gtests.appliances.presentation.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * Representation service for editable state
 *
 * @author g-tests
 */
public interface EditableStateService {

    @Transactional(readOnly = true)
    Optional<Map<String, Object>> get(String endpointId);

    @Transactional()
    Optional<Map<String, Object>> update(String endpointId, Map<String, Object> newState);
}
