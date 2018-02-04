package gtests.appliances.presentation.view;

import gtests.appliances.persistence.model.EndpointJob;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Map;

/**
 * Main representation for job resource.
 *
 * @author g-tests
 */
@Getter
@Setter
public class JobMainView {

    /**
     * {@link EndpointJob#getId()}
     */
    private Long id;

    /**
     * {@link EndpointJob#getProgram()}
     */
    @NotNull
    private Long program;

    /**
     * {@link EndpointJob#getParams()}
     */
    @NotNull
    private Map<String, Object> params;

    /**
     * {@link EndpointJob#getStarted()}
     */
    @Null
    private String started;

    /**
     * {@link EndpointJob#getFinished()}
     */
    @Null
    private String finished;

    /**
     * {@link EndpointJob#getExpectedToStart()}
     */
    private String expectedToStart;

    /**
     * {@link EndpointJob#getExpectedToFinish()}
     */
    @Null
    private String expectedToFinish;

}
