package gtests.appliances.presentation.mapper;

import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.presentation.view.JobMainView;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for job view and entity.
 * <p>
 * Follows the convention from
 * {@link gtests.appliances.presentation.mapper package documentation}
 *
 * @author g-tests
 */
@Mapper(
        uses = TemporalConventionsMapper.class,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface JobMapper {

    /**
     * Instance of this mapper
     */
    JobMapper JOB_MAPPER = Mappers.getMapper(JobMapper.class);

    /**
     * Converts entity to view
     */
    JobMainView entityToView(EndpointJob entity);

    /**
     * Maps view data onto the entity
     */
    @InheritInverseConfiguration
    @Mapping(target = "program", expression = "java(null)")
    void updateEntityWithView(@MappingTarget EndpointJob entity, JobMainView view);

    default EndpointProgram idToProgram(Long id) {
        return null;
    }

    default Long programToId(EndpointProgram program) {
        return program.getId();
    }
}
