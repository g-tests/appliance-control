package gtests.appliances.presentation.service;

import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.persistence.repository.ProgramRepo;
import gtests.appliances.presentation.view.ProgramMainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static gtests.appliances.presentation.mapper.ProgramMapper.PROGRAM_MAPPER;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * Representation service for program resource
 *
 * @author g-tests
 */
@Service
public class ProgramService {

    private EndpointRepo endpointRepo;
    private ProgramRepo programRepo;

    @Autowired
    public ProgramService(EndpointRepo endpointRepo, ProgramRepo programRepo) {
        this.endpointRepo = endpointRepo;
        this.programRepo = programRepo;
    }

    /**
     * Finds program by id and endpoint id
     *
     * @param endpointId endpoint id
     * @param programId  program id
     * @return Optional view of a program (empty if endpoint does not exist)
     */
    public Optional<ProgramMainView> get(String endpointId, Long programId) {
        return ofNullable(programRepo.findOneByIdAndEndpointId(programId, endpointId))
                .map(PROGRAM_MAPPER::entityToView);
    }

    /**
     * Finds all programs available for the endpoint
     *
     * @param endpointId endpoint id
     * @return Optional list of views (empty if endpoint does not exist)
     */
    public Optional<List<ProgramMainView>> list(String endpointId) {
        List<ProgramMainView> result = null;
        if (endpointRepo.exists(endpointId)) {
            result = programRepo.findAll()
                    .stream()
                    .map(PROGRAM_MAPPER::entityToView)
                    .collect(toList());
        }
        return ofNullable(result);
    }
}