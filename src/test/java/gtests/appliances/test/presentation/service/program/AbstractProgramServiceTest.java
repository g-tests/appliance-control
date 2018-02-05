package gtests.appliances.test.presentation.service.program;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.persistence.repository.ProgramRepo;
import gtests.appliances.presentation.mapper.ProgramMapper;
import gtests.appliances.presentation.service.impl.ProgramServiceImpl;
import gtests.appliances.presentation.view.ProgramMainView;
import gtests.appliances.test.util.data.TestDataPaths;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static gtests.appliances.test.util.data.EntityDataProvider.getFullEndpoint;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Program service tests
 *
 * @author g-tests
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractProgramServiceTest {

    protected Endpoint testEndpoint;
    protected EndpointProgram testProgram;
    protected ProgramMainView testProgramView;

    @Mock
    protected EndpointRepo endpointRepo;

    @Mock
    protected ProgramRepo programRepo;

    @InjectMocks
    protected ProgramServiceImpl programService;

    protected String testEndpointId = "testId";
    protected long testProgramId = 1366000L;

    @Before
    public void setUp() throws IOException {
        testEndpoint = getFullEndpoint(TestDataPaths.Entities.ENDPOINT_OVEN);
        testEndpoint.setId(testEndpointId);
        testProgram = testEndpoint.getAvailablePrograms().iterator().next();
        testProgram.setId(testProgramId);
        testProgramView = ProgramMapper.PROGRAM_MAPPER.entityToView(testProgram);
        mockReposResponse();
    }

    protected abstract void mockReposResponse();
}
