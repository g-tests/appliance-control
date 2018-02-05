package gtests.appliances.presentation.service;

import gtests.appliances.presentation.view.JobMainView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Representation service for job resource
 *
 * @author g-tests
 */
public interface JobService {

    @Transactional(readOnly = true)
    Optional<List<JobMainView>> list(String endpointId);

    @Transactional()
    Optional<JobMainView> create(JobMainView view, String endpointId);

    @Transactional(readOnly = true)
    Optional<JobMainView> read(String endpointId, Long jobId);

    @Transactional()
    Optional<JobMainView> update(JobMainView view, String endpointId);

    @Transactional()
    Optional<Boolean> delete(Long jobId, String endpointId);
}
