package gtests.appliances.test.presentation.service;

import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.presentation.view.JobMainView;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

/**
 * Tests for JobService in case when endpoint id exists,
 * but no content found by job id
 *
 * @author g-tests
 */
public class JobServiceJobsMissingResourceTests extends JobServiceAbstractTest {

    @Override
    protected void mockReposResponse() {
        when(endpointRepo.exists(eq(testEndpointId))).thenReturn(true); //endpoint found
        when(jobRepo.findByEndpointId(eq(testEndpointId))).thenReturn(emptyList());
        when(jobRepo
                .findOneByIdAndEndpointId(eq(testJobId), eq(testEndpointId)))
                .thenReturn(null);  // not found by id
        when(jobRepo
                .deleteOneByIdAndEndpointId(eq(testJobId), eq(testEndpointId)))
                .thenReturn(0L);    // nothing deleted

        when(jobRepo
                .save(any(EndpointJob.class)))
                .thenReturn(testJob);   // saving should still be possible
        when(programRepo
                .findOneByIdAndEndpointId(eq(testView.getProgram()), eq(testEndpointId)))
                .thenReturn(testJob.getProgram());  // program is queried while saving
    }

    @Test
    public void listEmptyIfNotFound() {
        Optional<List<JobMainView>> list = jobService.list(testEndpointId);
        assertTrue(list.isPresent());
        assertTrue(list.get().isEmpty());
    }

    @Test
    public void shouldCreateAnyways() {
        testView.setId(null);
        assertNotNull(testJob.getId());

        Optional<JobMainView> result = jobService.create(testView, testEndpointId);

        assertTrue(result.isPresent());
        assertEquals(testJob.getId(), result.get().getId());
        result.get().setId(testView.getId());
        assertViewEquals(result.get(), testView);
    }

    @Test
    public void getEmptyIfNotFound() {
        Optional<JobMainView> result = jobService.read(testEndpointId, testJobId);
        assertFalse(result.isPresent());
    }

    @Test
    public void updateEmptyIfNotFound() {
        assertNotNull(testView.getId());

        Optional<JobMainView> result = jobService.update(testView, testEndpointId);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldDeleteReportFalse() {
        Optional<Boolean> result = jobService.delete(testJobId, testEndpointId);
        assertTrue(result.isPresent());
        assertFalse(result.get());
    }
}
