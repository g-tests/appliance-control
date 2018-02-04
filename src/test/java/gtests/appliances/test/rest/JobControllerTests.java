package gtests.appliances.test.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gtests.appliances.persistence.model.EndpointJob;
import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.presentation.view.JobMainView;
import gtests.appliances.test.util.data.TestDataPaths;
import gtests.appliances.test.util.data.ViewDataProvider;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Jobs controller tests
 *
 * @author g-tests
 */
public class JobControllerTests extends AbstractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldAllowListing() throws Exception {
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/jobs";
        assertFalse(preloadedEndpoint.getPendingJobs().isEmpty());
        assertGotListOfSize(mockMvc.perform(get(url)), preloadedEndpoint.getPendingJobs().size());
    }

    @Test
    public void shouldFindExisting() throws Exception {
        EndpointJob job = preloadedEndpoint.getPendingJobs().iterator().next();
        assertNotNull(job.getId());
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/jobs/" + job.getId();
        assertGotObjectWithId(mockMvc.perform(get(url)), job.getId().intValue());
    }

    @Test
    public void shouldCreateNewAndUpdateIt() throws Exception {
        JobMainView jobMainView = ViewDataProvider.getJobMainView(TestDataPaths.Views.JOB_DEFAULT);
        jobMainView.setId(null);

        assertTrue("Test should have at least two programs available", preloadedEndpoint.getAvailablePrograms().size() > 1);
        Iterator<EndpointProgram> programs = preloadedEndpoint.getAvailablePrograms().iterator();
        EndpointProgram programOne = programs.next(),
                programTwo = programs.next();
        jobMainView.setProgram(programOne.getId());

        String url = "/endpoints/" + preloadedEndpoint.getId() + "/jobs";

        // post new job
        ResultActions result = mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsBytes(jobMainView))
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        // check it was created and take the response
        MvcResult creationResult = result.andExpect(status().isCreated()).andReturn();

        // locate new job
        String createdLocation = creationResult.getResponse().getHeader(LOCATION);

        // check it can be found
        mockMvc.perform(get(createdLocation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.program", Matchers.equalTo(jobMainView.getProgram().intValue())))
                .andReturn();

        // change program id to track update
        jobMainView.setProgram(programTwo.getId());

        // see if it will be updated
        mockMvc.perform(
                put(createdLocation)
                        .content(objectMapper.writeValueAsBytes(jobMainView))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204));

        // check it can be found
        mockMvc.perform(get(createdLocation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.program", Matchers.equalTo(jobMainView.getProgram().intValue())))
                .andReturn();
    }

    @Test
    public void shouldDeleteIfExist() throws Exception {
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/jobs";
        byte[] response = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        List<JobMainView> list = objectMapper.readValue(response, new TypeReference<List<JobMainView>>() {
        });
        JobMainView jobToDelete = list.get(0);

        String deleteUrl = url + "/" + jobToDelete.getId();
        mockMvc.perform(delete(deleteUrl))
                .andExpect(status().isNoContent());

        // once more
        mockMvc.perform(delete(deleteUrl))
                .andExpect(status().isNoContent());
    }

}
