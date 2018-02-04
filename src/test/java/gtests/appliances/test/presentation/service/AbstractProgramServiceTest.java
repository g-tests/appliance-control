package gtests.appliances.test.presentation.service;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.persistence.repository.ProgramRepo;
import gtests.appliances.presentation.mapper.ProgramMapper;
import gtests.appliances.presentation.service.EndpointService;
import gtests.appliances.presentation.service.ProgramService;
import gtests.appliances.presentation.view.EndpointMainView;
import gtests.appliances.presentation.view.ProgramMainView;
import gtests.appliances.test.util.data.TestDataPaths;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static gtests.appliances.test.util.data.EntityDataProvider.getFullEndpoint;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
    protected ProgramService programService;

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
