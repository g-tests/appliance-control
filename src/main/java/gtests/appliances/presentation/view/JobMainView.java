package gtests.appliances.presentation.view;

import gtests.appliances.persistence.model.EndpointJob;
import lombok.Getter;
import lombok.Setter;

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
    private Long program;

    /**
     * {@link EndpointJob#getParams()}
     */
    private Map<String, Object> params;

    /**
     * {@link EndpointJob#getStarted()}
     */
    private String started;

    /**
     * {@link EndpointJob#getFinished()}
     */
    private String finished;

    /**
     * {@link EndpointJob#getExpectedToStart()}
     */
    private String expectedToStart;

    /**
     * {@link EndpointJob#getExpectedToFinish()}
     */
    private String expectedToFinish;

}
