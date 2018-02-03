package gtests.appliances.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Endpoint program persistent entity
 *
 * @author g-tests
 */
@Entity
@Getter
@Setter
public class EndpointProgram implements BaseEntity<Long> {

    /**
     * Program id. Generated upon saving
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
     * Jobs performed
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "program")
    private Set<EndpointJob> jobs;

    /**
     * Human-readable name
     */
    private String name;

    /**
     * Human-readable description
     */
    private String description;

    /**
     * json-schema document describing the structure of parameters expected by the program
     */
    @Column(columnDefinition = "varchar")
    private String parameterScheme;

}
