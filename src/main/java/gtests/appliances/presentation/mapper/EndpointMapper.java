package gtests.appliances.presentation.mapper;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.presentation.view.EndpointMainView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for endpoint view and entity.
 *
 * Follows the convention from
 * {@link gtests.appliances.presentation.mapper package documentation}
 *
 * @author g-tests
 */
@Mapper
public interface EndpointMapper {

    /**
     * Instance of this mapper
     */
    EndpointMapper ENDPOINT_MAPPER = Mappers.getMapper(EndpointMapper.class);

    /**
     * Converts entity to view
     */
    @Mappings({
            @Mapping(target = "name", source = "id")
    })
    EndpointMainView entityToView(Endpoint entity);
}
