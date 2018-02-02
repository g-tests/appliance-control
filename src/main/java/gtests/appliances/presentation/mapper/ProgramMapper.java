package gtests.appliances.presentation.mapper;

import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.presentation.view.ProgramMainView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for program view and entity
 * <p>
 * Follows the convention from
 * {@link gtests.appliances.presentation.mapper package documentation}
 *
 * @author g-tests
 */
@Mapper(componentModel = "spring")
public interface ProgramMapper {

    /**
     * Instance of this mapper
     */
    ProgramMapper PROGRAM_MAPPER = Mappers.getMapper(ProgramMapper.class);

    /**
     * Converts entity to view
     */
    ProgramMainView entityToView(EndpointProgram entity);
}
