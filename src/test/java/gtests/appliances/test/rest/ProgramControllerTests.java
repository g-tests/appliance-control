package gtests.appliances.test.rest;

import gtests.appliances.persistence.model.EndpointProgram;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Programs controller tests
 *
 * @author g-tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgramControllerTests extends AbstractControllerTest {

    @Test
    public void shouldListPrograms() throws Exception {
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/programs";
        assertGotListOfSize(mockMvc.perform(get(url)), preloadedEndpoint.getAvailablePrograms().size());
    }

    @Test
    public void shouldFindProgram() throws Exception {
        EndpointProgram program = preloadedEndpoint.getAvailablePrograms().iterator().next();
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/programs/" + program.getId();
        assertGotObjectWithId(mockMvc.perform(get(url)), program.getId().intValue());
    }

    @Test
    public void shouldNotFindProgram() throws Exception {
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/programs/0";
        assertNotFoundResponse(mockMvc.perform(get(url)));
    }

    @Test
    public void shouldAllowReadsOnly() throws Exception {
        shouldNotAllow("/endpoints/whatever/programs", PUT, PATCH, POST, DELETE);
        shouldNotAllow("/endpoints/whatever/programs/0", PUT, PATCH, POST, DELETE);
    }
}
