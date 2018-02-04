package gtests.appliances.test.presentation.service.editablestate;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.repository.EndpointRepo;
import gtests.appliances.presentation.service.EditableStateService;
import gtests.appliances.test.util.data.EntityDataProvider;
import gtests.appliances.test.util.data.TestDataPaths;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for EditableStateService
 *
 * @author g-tests
 */
@RunWith(MockitoJUnitRunner.class)
public class EditableStateServiceTests {

    @Mock
    private EndpointRepo endpointRepo;

    @InjectMocks
    private EditableStateService service;

    private Endpoint testEndpoint;
    private Map<String, Object> testState;

    @Before
    public void setUp() throws IOException {
        testEndpoint = EntityDataProvider.getFullEndpoint(TestDataPaths.Entities.ENDPOINT_OVEN);
        testState = new HashMap<>();
        testState.put("test-key-1", "test-value-1");
        assertNotEquals(testState, testEndpoint.getEditableState());
    }

    @Test
    public void shouldGetEmptyOptional() {
        when(endpointRepo.findOneById(eq(testEndpoint.getId())))
                .thenReturn(Optional.empty());
        Optional<Map<String, Object>> result = service.get(testEndpoint.getId());
        assertFalse(result.isPresent());
        verify(endpointRepo, times(1)).findOneById(eq(testEndpoint.getId()));
        verifyNoMoreInteractions(endpointRepo);
    }

    @Test
    public void shouldGetOptional() {
        when(endpointRepo.findOneById(eq(testEndpoint.getId())))
                .thenReturn(Optional.of(testEndpoint));
        Optional<Map<String, Object>> result = service.get(testEndpoint.getId());
        assertTrue(result.isPresent());
        assertFalse(result.get() == testEndpoint.getEditableState());
        assertEquals(result.get(), testEndpoint.getEditableState());
        verify(endpointRepo, times(1)).findOneById(eq(testEndpoint.getId()));
        verifyNoMoreInteractions(endpointRepo);
    }

    @Test
    public void shouldNotUpdateReturnEmptyOptional() {
        when(endpointRepo.findOneById(eq(testEndpoint.getId())))
                .thenReturn(Optional.empty());
        assertFalse(
                service.update(testEndpoint.getId(), testState)
                        .isPresent());
        verify(endpointRepo, times(1))
                .findOneById(eq(testEndpoint.getId()));
        verifyNoMoreInteractions(endpointRepo);
    }

    @Test
    public void shouldUpdateReturnOptional() {
        when(endpointRepo.findOneById(eq(testEndpoint.getId())))
                .thenReturn(Optional.of(testEndpoint));
        when(endpointRepo.save(eq(testEndpoint)))
                .thenReturn(testEndpoint);
        assertTrue(
                service.update(testEndpoint.getId(), testState)
                        .isPresent());
        verify(endpointRepo, times(1)).findOneById(eq(testEndpoint.getId()));
        verify(endpointRepo, times(1)).save(eq(testEndpoint));
        verifyNoMoreInteractions(endpointRepo);
    }
}
