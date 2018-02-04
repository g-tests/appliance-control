package gtests.appliances.test.presentation.service.jobs;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.persistence.repository.JobRepo;
import gtests.appliances.persistence.repository.ProgramRepo;
import gtests.appliances.presentation.mapper.JobMapper;
import gtests.appliances.presentation.service.JobService;
import gtests.appliances.presentation.view.JobMainView;
import gtests.appliances.test.util.data.TestDataPaths;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static gtests.appliances.test.util.data.EntityDataProvider.getFullEndpoint;
import static org.junit.Assert.assertEquals;

/**
 * Base test for JobService
 *
 * @author g-tests
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractJobServiceTest {

    protected EndpointJob testJob;
    protected JobMainView testView;
    protected String testEndpointId;
    protected Long testJobId;

    @Mock
    protected JobRepo jobRepo;

    @Mock
    protected EndpointRepo endpointRepo;

    @Mock
    protected ProgramRepo programRepo;

    @InjectMocks
    protected JobService jobService;

    @Before
    public void setUp() throws IOException {
        Endpoint endpoint = getFullEndpoint(TestDataPaths.Entities.ENDPOINT_OVEN);
        testJob = endpoint.getPendingJobs().iterator().next();
        testJob.setId(42L);
        testView = JobMapper.JOB_MAPPER.entityToView(testJob);
        testEndpointId = endpoint.getId();
        testJobId = testJob.getId();
        mockReposResponse();
    }

    protected void assertViewEquals(JobMainView expected, JobMainView actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getParams(), actual.getParams());
        assertEquals(expected.getProgram(), actual.getProgram());
    }

    abstract protected void mockReposResponse();

}
