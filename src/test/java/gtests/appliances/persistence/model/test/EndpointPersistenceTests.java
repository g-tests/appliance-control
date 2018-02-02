package gtests.appliances.persistence.model.test;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.test.util.PersistenceTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static gtests.appliances.test.util.TestDataProvider.addJobsToEndpoint;
import static gtests.appliances.test.util.TestDataProvider.getEndpointWithPrograms;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Endpoint persistence tests
 *
 * @author g-tests
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class EndpointPersistenceTests {

    public static final String TEST_OBJECT = "oven";
    public static final String TEST_STRING = "quiteUniqueTestString#8123";
    public static final ZonedDateTime TEST_DATE_TIME = ZonedDateTime.now();

    @Autowired
    private TestEntityManager entityManager;

    private Endpoint endpoint;

    /**
     * Loads endpoint defined in test data, saves it
     * and clears the session for further testing
     */
    @Before
    public void setUpStructure() throws IOException {
        endpoint = getEndpointWithPrograms(TEST_OBJECT);
        // as jobs are associated with programs, we have to save programs first
        addJobsToEndpoint(endpoint, TEST_OBJECT);
        entityManager.persist(endpoint);
        entityManager.flush();
        entityManager.refresh(endpoint);
    }

    @After
    public void cleanUp() {
        entityManager.clear();
    }

    /**
     * Tests that endpoint fields and associations get persisted
     */
    @Test
    public void testEndpointFieldsPersistence() {
        PersistenceTester.of(Endpoint.class, endpoint.getId(), entityManager)
                .assertSavedField(Endpoint::getType, Endpoint::setType, TEST_STRING)
                .changeAndAssert(
                        before -> before.getCompletedJobs().clear(),
                        after -> after.getCompletedJobs().isEmpty())
                .changeAndAssert(
                        before -> before.getPendingJobs().clear(),
                        after -> after.getPendingJobs().isEmpty())
                .changeAndAssert(
                        before -> before.getAvailablePrograms().clear(),
                        after -> after.getAvailablePrograms().isEmpty());
    }

    /**
     * Tests that program removals cascade down to jobs
     */
    @Test
    public void testProgramCascading() {
        PersistenceTester.of(Endpoint.class, endpoint.getId(), entityManager).changeAndAssert(
                beforeSave -> beforeSave.getAvailablePrograms().clear(),
                afterSave ->
                        assertTrue(
                                afterSave.getCompletedJobs().isEmpty() &&
                                        afterSave.getPendingJobs().isEmpty() &&
                                        afterSave.getAvailablePrograms().isEmpty()));
    }

    /**
     * Tests that job data get persisted
     */
    @Test
    public void testJobFieldsPersistence() {
        EndpointJob testJob = endpoint.getCompletedJobs().iterator().next();
        Map<String, Object> testParamMap = new HashMap<>();
        testParamMap.put("FirstLevelParameter", TEST_STRING);
        HashMap<String, Object> subMap = new HashMap<>();
        subMap.put("subParameter", TEST_STRING);
        testParamMap.put("SecondLevelParameter", subMap);

        testJob.setParams(testParamMap);

        PersistenceTester.of(EndpointJob.class, testJob.getId(), entityManager)
                .assertSavedField(EndpointJob::getStarted, EndpointJob::setStarted, TEST_DATE_TIME)
                .assertSavedField(EndpointJob::getFinished, EndpointJob::setFinished, TEST_DATE_TIME)
                .assertSavedField(EndpointJob::getExpectedToStart, EndpointJob::setExpectedToStart, TEST_DATE_TIME)
                .assertSavedField(EndpointJob::getExpectedToFinish, EndpointJob::setExpectedToFinish, TEST_DATE_TIME)
                .changeAndAssert(beforeSave -> beforeSave.setParams(testParamMap),
                        afterSave -> assertEquals(testParamMap, afterSave.getParams()));
    }

    /**
     * Tests that program data gets persisted
     */
    @Test
    public void testProgramFieldsPersistence() {
        EndpointProgram testProgram = endpoint.getAvailablePrograms().iterator().next();
        PersistenceTester.of(EndpointProgram.class, testProgram.getId(), entityManager)
                .assertSavedField(EndpointProgram::getDescription, EndpointProgram::setDescription, TEST_STRING)
                .assertSavedField(EndpointProgram::getName, EndpointProgram::setName, TEST_STRING)
                .assertSavedField(EndpointProgram::getParameterScheme, EndpointProgram::setParameterScheme, TEST_STRING);

    }

    /**
     * Tests that job parameters map can handle necessary types
     */
    @Test
    public void testJobParamsTypesPersistence() {
        EndpointJob testJob = endpoint.getCompletedJobs().iterator().next();

        Map<String, Object> testParams = new HashMap<>();
        // expect it to work only with primitive values so far
        testParams.put("string", TEST_STRING);
        testParams.put("integer", 88);
        testParams.put("double", 13.0 / 17);
        testParams.put("boolean", true);

        PersistenceTester.of(EndpointJob.class, testJob.getId(), entityManager)
                .changeAndAssert(
                        before -> before.setParams(testParams),
                        after ->
                                testParams.forEach((key, value) ->
                                        assertEquals("Parameter '" + key + "' should not change upon persistence",
                                                value, after.getParams().get(key))));
    }
}
