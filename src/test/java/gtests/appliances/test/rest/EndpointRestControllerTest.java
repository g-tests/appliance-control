package gtests.appliances.test.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static gtests.appliances.presentation.ResourcePaths.Endpoints;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Endpoints controller tests
 *
 * @author g-tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EndpointRestControllerTest extends AbstractControllerTest {

    @Test
    public void shouldListEndpoints() throws Exception {
        mockMvc.perform(get(Endpoints.COLLECTION))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", not(empty())))
                .andReturn();
    }

    @Test
    public void shouldFindEndpoint() throws Exception {
        mockMvc.perform(get(Endpoints.ITEM, preloadedEndpoint.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", equalTo(preloadedEndpoint.getId())));
    }

    @Test
    public void shouldNotFindEndpoint() throws Exception {
        mockMvc.perform(get(Endpoints.ITEM, "nonExistentID"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAllowReadsOnly() throws Exception {
        shouldNotAllow(Endpoints.COLLECTION, POST, PUT, PATCH, DELETE);
        shouldNotAllow(Endpoints.COLLECTION + "/whatever", POST, PUT, PATCH, DELETE);
    }

}
