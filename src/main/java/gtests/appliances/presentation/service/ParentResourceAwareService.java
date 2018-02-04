package gtests.appliances.presentation.service;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Abstract implementation of a service working with a sub-resource
 *
 * @author g-tests
 */
public abstract class ParentResourceAwareService<PID extends Serializable> {

    /**
     * @param parentId parent entity id
     * @return true if a parent with this id exists
     */
    protected abstract boolean isParentExist(PID parentId);

    /**
     * @param parentId parent id
     * @param supplier protected supplier
     * @param <T>      any type
     * @return Optional with supplied value (empty if parent with specified id does not exist)
     */
    protected <T> Optional<T> getIfParentExist(PID parentId, Supplier<T> supplier) {
        return isParentExist(parentId) ?
                Optional.of(supplier.get()) :
                Optional.empty();
    }
}
