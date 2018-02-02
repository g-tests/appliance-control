package gtests.appliances.persistence.model;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.Map;

/**
 * Endpoint persistent entity.
 * Represents an appliance in IoT network
 *
 * @author g-tests
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Endpoint implements BaseEntity<String> {

    /**
     * Identifier of the endpoint.
     * Should not be empty upon saving
     */
    @Id
    private String id;

    /**
     * Human-readable type of appliance
     */
    private String type;

    /**
     * Programs the appliance can perform
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EndpointProgram> availablePrograms;

    /**
     * Tasks the appliance performs now or is scheduled to perform later
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "endpoint")
    @Where(clause = "finished is null")
    private Set<EndpointJob> pendingJobs;

    /**
     * Tasks the appliance completed
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "endpoint")
    @Where(clause = "finished is not null")
    private Set<EndpointJob> completedJobs;

    /**
     * Parameters of the appliance that can be changed directly
     */
    @Type(type = "json")
    @Column(columnDefinition = "varchar")
    private Map<String, Object> editableState;

}
