package gtests.appliances.model;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;

/**
 * Interface for all persistence entities
 *
 * @author g-tests
 */
@TypeDef(name = "json", typeClass = JsonStringType.class)
public interface BaseEntity<ID extends Serializable> {

    ID getId();

}
