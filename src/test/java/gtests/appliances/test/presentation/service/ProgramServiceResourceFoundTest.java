package gtests.appliances.test.presentation.service;

import gtests.appliances.presentation.view.ProgramMainView;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for program service when all resources found
 *
 * @author g-tests
 */
public class ProgramServiceResourceFoundTest extends AbstractProgramServiceTest {

    @Override
    protected void mockReposResponse() {
        when(endpointRepo.exists(any())).thenReturn(true);
        when(programRepo.findByEndpointId(any())).thenReturn(singletonList(testProgram));
        when(programRepo.findOneByIdAndEndpointId(any(), any())).thenReturn(testProgram);
    }

    @Test
    public void shouldListOptionalList() {
        Optional<List<ProgramMainView>> list = programService.list(testEndpointId);
        assertTrue(list.isPresent());
        verify(endpointRepo, times(1)).exists(eq(testEndpointId));
        verify(programRepo, times(1)).findByEndpointId(eq(testEndpointId));
        verifyNoMoreInteractions(endpointRepo);
        verifyNoMoreInteractions(programRepo);
        assertEquals(list.get().size(), 1);
    }

    @Test
    public void shouldGetEmptyOptional() {
        Optional<ProgramMainView> result = programService.get(testEndpointId, testProgramId);
        assertTrue(result.isPresent());
        verify(programRepo, times(1)).findOneByIdAndEndpointId(eq(testProgramId), eq(testEndpointId));
        verifyNoMoreInteractions(programRepo);
        assertEquals(result.get().getParameterScheme(), testProgramView.getParameterScheme());
    }
}
