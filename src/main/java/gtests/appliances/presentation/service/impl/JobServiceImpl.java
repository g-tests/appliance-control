package gtests.appliances.presentation.service.impl;

import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.persistence.repository.JobRepo;
import gtests.appliances.persistence.repository.ProgramRepo;
import gtests.appliances.presentation.service.JobService;
import gtests.appliances.presentation.view.JobMainView;
import gtests.appliances.validation.JsonValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static gtests.appliances.presentation.mapper.JobMapper.JOB_MAPPER;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * JobService implementation
 *
 * @author g-tests
 */
@Service
public class JobServiceImpl extends ParentResourceAwareService<String> implements JobService {

    private EndpointRepo endpointRepo;
    private JobRepo jobRepo;
    private ProgramRepo programRepo;

    @Autowired
    public JobServiceImpl(EndpointRepo endpointRepo, JobRepo jobRepo, ProgramRepo programRepo) {
        this.endpointRepo = endpointRepo;
        this.jobRepo = jobRepo;
        this.programRepo = programRepo;
    }

    /**
     * Finds all jobs contained in the endpoint
     *
     * @param endpointId endpoint id
     * @return Optional list of views (empty if endpoint does not exist)
     */
    @Override
    public Optional<List<JobMainView>> list(String endpointId) {
        return getIfParentExist(endpointId, () ->
                jobRepo.findPendingByEndpointId(endpointId)
                        .stream()
                        .map(JOB_MAPPER::entityToView)
                        .collect(toList()));
    }

    /**
     * Creates new job with data from specified representation
     *
     * @param view       job representation
     * @param endpointId endpoint id
     * @return Optional view of saved job (empty if endpoint does not exist)
     */
    @Override
    public Optional<JobMainView> create(JobMainView view, String endpointId) {
        EndpointJob entity = new EndpointJob();
        return upsertEntity(view, endpointId, entity);
    }

    /**
     * Finds job by id and endpoint id
     *
     * @param endpointId endpoint id
     * @param jobId      job id
     * @return Optional view of job (empty if endpoint does not exist)
     */
    @Override
    public Optional<JobMainView> read(String endpointId, Long jobId) {
        EndpointJob entity = jobRepo.findOneByIdAndEndpointId(jobId, endpointId);
        return ofNullable(JOB_MAPPER.entityToView(entity));
    }

    /**
     * Updates existing job with data from specified representation
     *
     * @param view       representation
     * @param endpointId endpoint id
     * @return Optional view of updated job (empty if endpoint does not exist)
     */
    @Override
    public Optional<JobMainView> update(JobMainView view, String endpointId) {
        return ofNullable(jobRepo.findOneByIdAndEndpointId(view.getId(), endpointId))
                .flatMap(entity -> upsertEntity(view, endpointId, entity));
    }

    /**
     * Removes job by id and endpoint id
     *
     * @param jobId      job id
     * @param endpointId endpoint id
     * @return Optional flag indicating that something was deleted (empty if endpoint does not exist)
     */
    @Override
    public Optional<Boolean> delete(Long jobId, String endpointId) {
        return getIfParentExist(endpointId, () ->
                jobRepo.deleteOneByIdAndEndpointId(jobId, endpointId) > 0);
    }

    private Optional<JobMainView> upsertEntity(JobMainView view, String endpointId, EndpointJob entity) {
        return getIfParentExist(endpointId, () -> {
            JOB_MAPPER.updateEntityWithView(entity, view);
            EndpointProgram program = programRepo.findOneByIdAndEndpointId(view.getProgram(), endpointId);
            checkProgramParams(entity, program);
            entity.setProgram(program);
            entity.setEndpoint(endpointRepo.findOne(endpointId));
            EndpointJob saved = jobRepo.save(entity);
            return JOB_MAPPER.entityToView(saved);
        });
    }

    private void checkProgramParams(EndpointJob entity, EndpointProgram program) {
        JsonValidationUtil.validateMap(entity.getParams(), program.getParameterScheme());
    }

    @Override
    protected boolean isParentExist(String parentId) {
        return endpointRepo.exists(parentId);
    }
}
