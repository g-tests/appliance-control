package gtests.appliances.presentation.mapper.tests;

import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.presentation.view.JobMainView;
import gtests.appliances.util.TemporalConventions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.ZonedDateTime;

import static gtests.appliances.presentation.mapper.JobMapper.JOB_MAPPER;
import static gtests.appliances.test.util.EntityDataProvider.getFullEndpoint;
import static gtests.appliances.test.util.ViewDataProvider.getJobMainView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Tests conversion between entities and views for jobs
 *
 * @author g-tests
 */
public class JobMapperTest {

    public static final String DIRECTORY_NAME = "oven";

    private EndpointJob jobEntity;

    @Before
    public void setUp() throws IOException {
        jobEntity = getFullEndpoint(DIRECTORY_NAME)
                .getCompletedJobs()
                .iterator()
                .next();
        jobEntity.setId(123456L);
        jobEntity.getProgram().setId(98765L);
    }

    @After
    public void cleanUp() {
        jobEntity = null;
    }

    @Test
    public void testEntityToViewMapping() {
        JobMainView view = JOB_MAPPER.entityToView(jobEntity);
        assertEquals(jobEntity.getId(), view.getId());
        assertFalse("Maps must not be the same", jobEntity.getParams() == view.getParams());
        assertEquals(jobEntity.getParams(), view.getParams());
        assertEquals(jobEntity.getProgram().getId(), view.getProgram());

        assertZDTConverted(jobEntity.getStarted(), view.getStarted());
        assertZDTConverted(jobEntity.getFinished(), view.getFinished());
        assertZDTConverted(jobEntity.getExpectedToStart(), view.getExpectedToStart());
        assertZDTConverted(jobEntity.getExpectedToFinish(), view.getExpectedToFinish());
    }

    @Test
    public void testViewToEntityMapping() {
        JobMainView view = getJobMainView(DIRECTORY_NAME);
        JOB_MAPPER.updateEntityWithView(jobEntity, view);
        assertEquals(view.getId(), jobEntity.getId());
        assertFalse("Maps must not be the same", jobEntity.getParams() == view.getParams());
        assertEquals(view.getParams(), jobEntity.getParams());

        // the program should be null-ed
        // to be assigned manually by invoking code
        assertNull(jobEntity.getProgram());

        assertZDTConverted(jobEntity.getStarted(), view.getStarted());
        assertZDTConverted(jobEntity.getFinished(), view.getFinished());
        assertZDTConverted(jobEntity.getExpectedToStart(), view.getExpectedToStart());
        assertZDTConverted(jobEntity.getExpectedToFinish(), view.getExpectedToFinish());
    }

    private void assertZDTConverted(ZonedDateTime zonedDateTime, String string) {
        String serialized = TemporalConventions.asString(zonedDateTime);
        String msg = "DateTime should be serialized as an ISO-compliant zoned timestamp";
        assertEquals(msg, serialized, string);
    }
}
