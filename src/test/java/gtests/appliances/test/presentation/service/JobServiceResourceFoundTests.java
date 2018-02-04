package gtests.appliances.test.presentation.service;

import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.presentation.view.JobMainView;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

/**
 * JobService tests for case when some jobs exist
 *
 * @author g-tests
 */
public class JobServiceResourceFoundTests extends JobServiceAbstractTest {

    @Override
    protected void mockReposResponse() {
        when(endpointRepo.exists(eq(testEndpointId))).thenReturn(true);
        when(jobRepo
                .save(any(EndpointJob.class)))
                .thenReturn(testJob);
        when(jobRepo.findPendingByEndpointId(eq(testEndpointId))).thenReturn(singletonList(testJob));
        when(jobRepo
                .findOneByIdAndEndpointId(eq(testJobId), eq(testEndpointId)))
                .thenReturn(testJob);
        when(jobRepo
                .deleteOneByIdAndEndpointId(eq(testJobId), eq(testEndpointId)))
                .thenReturn(1L);
        when(programRepo
                .findOneByIdAndEndpointId(eq(testView.getProgram()), eq(testEndpointId)))
                .thenReturn(testJob.getProgram());
    }

    @Test
    public void shouldListWhenFound() {
        Optional<List<JobMainView>> list = jobService.list(testEndpointId);
        assertTrue(list.isPresent());
        assertEquals(list.get().size(), 1);
        assertViewEquals(testView, list.get().get(0));
    }

    @Test
    public void shouldGetWhenFound() {
        Optional<JobMainView> result = jobService.read(testEndpointId, testJobId);
        assertTrue(result.isPresent());
        assertViewEquals(testView, result.get());
    }

    @Test
    public void shouldUpdateWhenFound() {
        assertNotNull(testView.getId());

        Optional<JobMainView> result = jobService.update(testView, testEndpointId);
        assertTrue(result.isPresent());
        assertViewEquals(testView, result.get());
    }

    @Test
    public void shouldDeleteWhenFound() {
        Optional<Boolean> result = jobService.delete(testJobId, testEndpointId);
        assertTrue(result.isPresent());
        assertTrue(result.get());
    }
}
