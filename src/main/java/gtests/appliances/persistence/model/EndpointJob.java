package gtests.appliances.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Endpoint job persistent entity
 *
 * @author g-tests
 */
@Entity
@Getter
@Setter
public class EndpointJob implements BaseEntity<Long> {

    /**
     * Job identifier. Generated upon saving
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Owning endpoint
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Endpoint endpoint;

    /**
     * Related program
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    private EndpointProgram program;

    /**
     * Customizable program parameters
     */
    @Type(type = "json")
    @Column(columnDefinition = "varchar")
    private Map<String, Object> params;

    /**
     * Time when job is expected to start
     */
    private ZonedDateTime expectedToStart;

    /**
     * Time when job actually started
     */
    private ZonedDateTime started;

    /**
     * Time when job is expected to finish
     */
    private ZonedDateTime expectedToFinish;

    /**
     * Time when job actually finished
     */
    private ZonedDateTime finished;

}
