package gtests.appliances.presentation.view;

import gtests.appliances.persistence.model.EndpointProgram;
import lombok.Getter;
import lombok.Setter;

/**
 * Main representation for program resource
 *
 * @author g-tests
 */
@Getter
@Setter
public class ProgramMainView {
    /**
     * {@link EndpointProgram#getId()}
     */
    private Long id;

    /**
     * {@link EndpointProgram#getName()}
     */
    private String name;

    /**
     * {@link EndpointProgram#getDescription()}
     */
    private String description;
    /**
     * {@link EndpointProgram#getParameterScheme()}
     */
    private String parameterScheme;

}
