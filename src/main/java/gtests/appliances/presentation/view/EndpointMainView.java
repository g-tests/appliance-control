package gtests.appliances.presentation.view;

import gtests.appliances.persistence.model.Endpoint;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Main representation for endpoint resource
 *
 * @author g-tests
 */
@Getter
@Setter
public class EndpointMainView {

    /**
     * {@link Endpoint#getId()}
     */
    private String name;

    /**
     * {@link Endpoint#getType()}
     */
    private String type;

    /**
     * {@link Endpoint#getStateSchema()}
     */
    private String stateSchema;

    /**
     * {@link Endpoint#getId()}
     */
    private Map<String, Object> editableState;

}
