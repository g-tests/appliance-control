package gtests.appliances.test.presentation.service.program;

import gtests.appliances.presentation.view.ProgramMainView;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for program service when all resources found
 *
 * @author g-tests
 */
public class ProgramServiceResourceNotFoundTest extends AbstractProgramServiceTest {

    @Override
    protected void mockReposResponse() {
        when(endpointRepo.exists(any())).thenReturn(true);
        when(programRepo.findByEndpointId(any())).thenReturn(emptyList());
        when(programRepo.findOneByIdAndEndpointId(any(), any())).thenReturn(null);
    }

    @Test
    public void shouldListOptionalEmptyList() {
        Optional<List<ProgramMainView>> list = programService.list(testEndpointId);
        assertTrue(list.isPresent());
        verify(endpointRepo, times(1)).exists(eq(testEndpointId));
        verify(programRepo, times(1)).findByEndpointId(eq(testEndpointId));
        verifyNoMoreInteractions(endpointRepo);
        verifyNoMoreInteractions(programRepo);
        assertTrue(list.get().isEmpty());
    }

    @Test
    public void shouldGetEmptyOptional() {
        Optional<ProgramMainView> result = programService.get(testEndpointId, testProgramId);
        assertFalse(result.isPresent());
        verify(programRepo, times(1)).findOneByIdAndEndpointId(eq(testProgramId), eq(testEndpointId));
        verifyNoMoreInteractions(programRepo);
    }
}
