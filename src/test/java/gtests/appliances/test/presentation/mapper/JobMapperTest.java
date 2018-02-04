package gtests.appliances.test.presentation.mapper;

import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.presentation.view.JobMainView;
import gtests.appliances.test.util.data.TestDataPaths;
import gtests.appliances.util.TemporalConventions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.ZonedDateTime;

import static gtests.appliances.presentation.mapper.JobMapper.JOB_MAPPER;
import static gtests.appliances.test.util.data.EntityDataProvider.getFullEndpoint;
import static gtests.appliances.test.util.data.ViewDataProvider.getJobMainView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests conversion between entities and views for jobs
 *
 * @author g-tests
 */
public class JobMapperTest {

    private EndpointJob testEntity;
    private JobMainView testView;

    @Before
    public void setUp() throws IOException {
        testEntity = getFullEndpoint(TestDataPaths.Entities.ENDPOINT_OVEN)
                .getCompletedJobs()
                .iterator()
                .next();
        testEntity.setId(123456L);
        testEntity.getProgram().setId(98765L);

        testView = getJobMainView(TestDataPaths.Views.JOB_MOCK);
    }

    @After
    public void cleanUp() {
        testEntity = null;
        testView = null;
    }

    @Test
    public void testFullEntityToViewMapping() {
        testView = JOB_MAPPER.entityToView(testEntity);
        assertEntityToView();
    }

    @Test
    public void testMinimalEntityToViewMapping() {
        testEntity.setParams(null);
        testEntity.setFinished(null);
        testEntity.setStarted(null);
        testView = JOB_MAPPER.entityToView(testEntity);
        assertEntityToView();
    }

    @Test
    public void testFullViewToEntityMapping() {
        JOB_MAPPER.updateEntityWithView(testEntity, testView);
        assertViewToEntity();
    }

    @Test
    public void testMinimalViewToEntityMapping() {
        testView.setParams(null);
        testView.setFinished(null);
        testView.setStarted(null);
        JOB_MAPPER.updateEntityWithView(testEntity, testView);
        assertViewToEntity();
    }

    private void assertEntityToView() {
        assertEquals(testEntity.getId(), testView.getId());
        assertTrue("Maps must not be the same if present", testEntity.getParams() != testView.getParams() || testEntity.getParams() == null);
        assertEquals(testEntity.getParams(), testView.getParams());
        assertEquals(testEntity.getProgram().getId(), testView.getProgram());

        assertZDTConverted(testEntity.getStarted(), testView.getStarted());
        assertZDTConverted(testEntity.getFinished(), testView.getFinished());
        assertZDTConverted(testEntity.getExpectedToStart(), testView.getExpectedToStart());
        assertZDTConverted(testEntity.getExpectedToFinish(), testView.getExpectedToFinish());
    }

    private void assertViewToEntity() {
        assertEquals(testView.getId(), testEntity.getId());
        assertTrue("Maps must not be the same if present", testEntity.getParams() != testView.getParams() || testView.getParams() == null);
        assertEquals(testView.getParams(), testEntity.getParams());

        // the program should be null-ed
        // to be assigned manually by invoking code
        assertNull(testEntity.getProgram());

        assertZDTConverted(testEntity.getStarted(), testView.getStarted());
        assertZDTConverted(testEntity.getFinished(), testView.getFinished());
        assertZDTConverted(testEntity.getExpectedToStart(), testView.getExpectedToStart());
        assertZDTConverted(testEntity.getExpectedToFinish(), testView.getExpectedToFinish());
    }

    private void assertZDTConverted(ZonedDateTime zonedDateTime, String string) {
        String serialized = zonedDateTime != null ?
                TemporalConventions.asString(zonedDateTime) :
                null;
        String msg = "DateTime should be serialized as an ISO-compliant zoned timestamp";
        assertEquals(msg, serialized, string);
    }
}
