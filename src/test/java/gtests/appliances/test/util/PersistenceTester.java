package gtests.appliances.test.util;

import gtests.appliances.persistence.model.BaseEntity;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Wrapper for testing entity's persistence.
 * <p>
 * NB: This wrapper does a lot of things with JPA first-level cache,
 * like calling flush() and clear() - do not use it along with any logic
 * sensitive to these session changes
 *
 * @author g-tests
 */
public class PersistenceTester<E extends BaseEntity> {

    private final TestEntityManager em;
    private E testEntity;
    private Class<E> entityType;


    private PersistenceTester(Class<E> entityType, Serializable testEntityId, TestEntityManager em) {
        em.flush();
        em.clear();
        this.em = em;
        this.entityType = entityType;
        this.testEntity = em.find(entityType, testEntityId);

        assertNotNull("Test entity with id " + testEntityId + " should exist", this.entityType);
    }

    /**
     * Factory method for getting an instance of the tester
     *
     * @param entityType    entity type
     * @param testEntityId  test entity id
     * @param entityManager entity manager instance
     * @param <E>           entity generic type
     * @return new instance of the tester
     */
    public static <E extends BaseEntity<?>> PersistenceTester<E> of(
            Class<E> entityType,
            Serializable testEntityId,
            TestEntityManager entityManager) {
        return new PersistenceTester<>(entityType, testEntityId, entityManager);
    }

    /**
     * Sets the field value, saves the entity and asserts that the value is saved.
     * <p>
     * Additionally, asserts that before the value is set the field has a different value.
     *
     * @param getter getter to get the value
     * @param setter setter to set
     * @param value  value to be set and asserted
     * @param <T>    property type
     * @return same tester instance for chaining
     */
    public <T> PersistenceTester<E> assertSavedField(Function<E, T> getter, BiConsumer<E, T> setter, T value) {
        String failMsgBefore = "Field already had provided value. Alter the value provided";
        String failMsgAfter = "Field value was not saved";
        assertSavedChange(
                toBeChanged -> setter.accept(toBeChanged, value),
                beforeSave -> assertFalse(failMsgBefore, getter.apply(beforeSave).equals(value)),
                afterSave -> assertTrue(failMsgAfter, getter.apply(afterSave).equals(value)));
        return this;
    }

    /**
     * Performs supplied change, saves the entity and invokes supplied consumer.
     *
     * @param change            consumer performing a change
     * @param assertingConsumer consumer asserting a change
     * @return same tester instance for chaining
     */
    public PersistenceTester<E> changeAndAssert(Consumer<E> change, Consumer<E> assertingConsumer) {
        assertSavedChange(change, ignored -> {
        }, assertingConsumer);
        return this;
    }

    /**
     * Performs supplied change, saves the entity and asserts that supplied predicate is true.
     * Additionally, asserts that before the change predicate is false
     *
     * @param change consumer performing a change
     * @param check  predicate testing a change
     * @return same tester instance for chaining
     */
    public PersistenceTester<E> assertSavedChange(Consumer<E> change, Predicate<E> check) {
        String failMsgBefore = "Entity passed the check before the change. Alter the change or the check";
        String failMsgAfter = "Entity failed the check after the change";
        assertSavedChange(change,
                beforeSave -> assertFalse(failMsgBefore, check.test(beforeSave)),
                afterSave -> assertTrue(failMsgAfter, check.test(afterSave)));
        return this;
    }

    /**
     * Invokes assertionBeforeSave, performs the change and invokes assertionAfterSave
     *
     * @param change              consumer performing change
     * @param assertionBeforeSave consumer performing assertions before change
     * @param assertionAfterSave  consumer performing assertions after change
     * @return same tester instance for chaining
     */
    public PersistenceTester<E> assertSavedChange(Consumer<E> change, Consumer<E> assertionBeforeSave, Consumer<E> assertionAfterSave) {
        // save to sync all previous changes (if any)
        E entityBeforeChange = em.persist(testEntity);
        // check precondition
        assertionBeforeSave.accept(entityBeforeChange);
        // make the change
        change.accept(entityBeforeChange);
        // flush everything
        em.flush();
        // clear everything
        em.clear();
        // load entity from anew
        E entityAfterSave = em.find(entityType, entityBeforeChange.getId());
        // check postcondition
        assertionAfterSave.accept(entityAfterSave);
        // pass attached entity forth
        testEntity = entityAfterSave;
        return this;
    }
}
