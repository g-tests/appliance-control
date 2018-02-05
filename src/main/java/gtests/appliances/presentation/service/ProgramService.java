package gtests.appliances.presentation.service;

import gtests.appliances.presentation.view.ProgramMainView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Representation service for program resource
 *
 * @author g-tests
 */
public interface ProgramService {

    @Transactional(readOnly = true)
    Optional<ProgramMainView> get(String endpointId, Long programId);

    @Transactional(readOnly = true)
    Optional<List<ProgramMainView>> list(String endpointId);
}
