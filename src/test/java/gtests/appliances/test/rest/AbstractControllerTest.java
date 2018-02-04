package gtests.appliances.test.rest;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.persistence.repository.EndpointRepo;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.Serializable;

import static gtests.appliances.test.util.data.EntityDataProvider.getFullEndpoint;
import static gtests.appliances.test.util.data.TestDataPaths.Entities;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base class for REST controllers tests
 *
 * @author g-tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public abstract class AbstractControllerTest {

    @Autowired
    protected EndpointRepo endpointRepo;

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    protected Endpoint preloadedEndpoint;

    @Before
    public void setUp() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
        preloadedEndpoint = getFullEndpoint(Entities.ENDPOINT_OVEN);
        endpointRepo.save(preloadedEndpoint);
        preloadedEndpoint = endpointRepo.findOne(preloadedEndpoint.getId());
    }

    @After
    public void cleanUp() {
        endpointRepo.delete(preloadedEndpoint);
    }

    protected void shouldNotAllow(String url, HttpMethod... unsupportedMethods) throws Exception {
        for (HttpMethod method : unsupportedMethods) {
            mockMvc.perform(request(method, url))
                    .andExpect(status().isMethodNotAllowed());
        }
    }

    protected void assertNotFoundResponse(ResultActions result) throws Exception {
        result.andExpect(status().isNotFound());
    }

    protected void assertGotObjectWithId(ResultActions result, Serializable id) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", equalTo(id)));
    }

    protected void assertGotListOfSize(ResultActions result, int size) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(size)));
    }
}
