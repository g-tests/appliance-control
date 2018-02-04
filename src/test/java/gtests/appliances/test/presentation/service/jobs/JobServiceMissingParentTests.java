package gtests.appliances.test.presentation.service.jobs;

import gtests.appliances.persistence.repository.JobRepo;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * JobService tests for case when endpoint with specified id
 * does not exist
 *
 * @author g-tests
 */
public class JobServiceMissingParentTests extends AbstractJobServiceTest {


    @Override
    protected void mockReposResponse() {
        when(endpointRepo.exists(testEndpointId)).thenReturn(false);
    }

    @Test
    public void shouldListEmptyOptional() {
        assertOnlyExistenceCheck(jobService.list(testEndpointId));
    }

    @Test
    public void shouldCreateEmptyOptional() {
        assertOnlyExistenceCheck(jobService.create(testView, testEndpointId));
    }

    @Test
    public void shouldGetEmptyOptional() {
        assertOnlyInvocation(
                jobService.read(testEndpointId, 42L),
                repo -> repo.findOneByIdAndEndpointId(any(), eq(testEndpointId)));
    }

    @Test
    public void shouldUpdateEmptyOptional() {
        assertOnlyInvocation(
                jobService.update(testView, testEndpointId),
                repo -> repo.findOneByIdAndEndpointId(any(), eq(testEndpointId)));
    }

    @Test
    public void shouldDeleteEmptyOptional() {
        assertOnlyExistenceCheck(jobService.delete(42L, testEndpointId));
    }

    private void assertOnlyExistenceCheck(Optional<?> result) {
        assertFalse(result.isPresent());

        verify(endpointRepo, times(1)).exists(eq(testEndpointId));
        verifyNoMoreInteractions(endpointRepo);
        verifyZeroInteractions(jobRepo);
    }

    private void assertOnlyInvocation(Optional<?> result, Consumer<JobRepo> invocation) {
        assertFalse(result.isPresent());
        invocation.accept(verify(jobRepo, times(1)));
        verifyNoMoreInteractions(jobRepo);
    }
}
