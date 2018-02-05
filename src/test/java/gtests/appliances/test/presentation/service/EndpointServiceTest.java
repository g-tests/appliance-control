package gtests.appliances.test.presentation.service;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.presentation.mapper.EndpointMapper;
import gtests.appliances.presentation.service.impl.EndpointServiceImpl;
import gtests.appliances.presentation.view.EndpointMainView;
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
 * Endpoint service tests
 *
 * @author g-tests
 */
@RunWith(MockitoJUnitRunner.class)
public class EndpointServiceTest {

    private Endpoint testEndpoint;
    private EndpointMainView testView;

    @Mock
    private EndpointRepo endpointRepo;

    @InjectMocks
    private EndpointServiceImpl endpointService;

    @Before
    public void setUp() throws IOException {
        testEndpoint = getFullEndpoint(TestDataPaths.Entities.ENDPOINT_OVEN);
        testEndpoint.setId("testId");
        testView = EndpointMapper.ENDPOINT_MAPPER.entityToView(testEndpoint);
    }

    @Test
    public void shouldListEmptyList() {
        when(endpointRepo.findAll()).thenReturn(emptyList());

        List<EndpointMainView> list = endpointService.list();

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void shouldListNotEmptyList() {
        when(endpointRepo.findAll()).thenReturn(singletonList(testEndpoint));

        List<EndpointMainView> list = endpointService.list();

        assertNotNull(list);
        assertEquals(1, list.size());
        assertNotNull(list.get(0));
        assertViewEquals(list.get(0), testView);
    }

    @Test
    public void shouldGetEmptyOptional() {
        when(endpointRepo.findOne(any())).thenReturn(null);

        Optional<EndpointMainView> result = endpointService.get(testEndpoint.getId());

        assertFalse(result.isPresent());

        verify(endpointRepo, times(1)).findOne(testEndpoint.getId());
        verifyNoMoreInteractions(endpointRepo);
    }

    @Test
    public void shouldGetOptionalView() {
        when(endpointRepo.findOne(any())).thenReturn(testEndpoint);

        Optional<EndpointMainView> result = endpointService.get("any");

        assertTrue(result.isPresent());
        assertViewEquals(result.get(), testView);
    }

    public void assertViewEquals(EndpointMainView expected, EndpointMainView actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getEditableState(), actual.getEditableState());
    }
}
