package gtests.appliances.test.presentation.service.program;

import gtests.appliances.presentation.view.ProgramMainView;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for program service when no parent endpoint found
 *
 * @author g-tests
 */
public class ProgramServiceMissingParentTest extends AbstractProgramServiceTest {

    @Override
    protected void mockReposResponse() {
        when(endpointRepo.exists(any())).thenReturn(false);
    }

    @Test
    public void shouldListEmptyOptional() {
        Optional<List<ProgramMainView>> list = programService.list(testEndpointId);
        assertFalse(list.isPresent());
        verify(endpointRepo, times(1)).exists(eq(testEndpointId));
        verifyZeroInteractions(programRepo);
    }

    @Test
    public void shouldGetEmptyOptional() {
        Optional<ProgramMainView> result = programService.get(testEndpointId, testProgramId);
        assertFalse(result.isPresent());
        verify(programRepo, times(1)).findOneByIdAndEndpointId(eq(testProgramId), eq(testEndpointId));
    }
}
