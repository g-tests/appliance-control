package gtests.appliances.test.documentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gtests.appliances.presentation.view.JobMainView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static com.google.common.net.HttpHeaders.LOCATION;
import static gtests.appliances.presentation.ResourcePaths.Endpoints;
import static gtests.appliances.presentation.ResourcePaths.Ids;
import static gtests.appliances.presentation.ResourcePaths.Jobs;
import static gtests.appliances.presentation.ResourcePaths.Programs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Spring restdocs documentation test
 *
 * @author g-tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ApiDocumentation {

    public static final ObjectMapper MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static final String ENDPOINT_ID = "wash-machine";
    public static final Long PROGRAM_ID = 3L;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    protected WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void documentEndpointsList() throws Exception {
        mockMvc.perform(get(Endpoints.COLLECTION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(
                        document("endpoints-collection",
                                responseFields(
                                        subsectionWithPath("[]").description("List of all endpoints available")//,
                                )));

        mockMvc.perform(get(Endpoints.ITEM, ENDPOINT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(ENDPOINT_ID)))
                .andDo(
                        document("endpoint-item",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint")
                                ),
                                responseFields(
                                        fieldWithPath("name").description("Name of the endpoint used as identifier"),
                                        fieldWithPath("type").description("Type of the endpoint"),
                                        fieldWithPath("stateSchema").description("Json-schema describing structure of editable state"),
                                        subsectionWithPath("editableState").description("Editable state of the endpoint")
                                )));

        String editableStateDescription = "Editable state of the endpoint. Its content " +
                "can be different and depends on functionality provided by the endpoint";
        mockMvc.perform(get(Endpoints.EDITABLE_STATE, ENDPOINT_ID))
                .andExpect(status().isOk())
                .andDo(
                        document("endpoint-state",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint")
                                ),
                                responseFields(
                                        subsectionWithPath("*").description(editableStateDescription)
                                )));

        mockMvc.perform(put(Endpoints.EDITABLE_STATE, ENDPOINT_ID)
                .content("{\"sound\" : \"off\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204))
                .andDo(
                        document("endpoint-state-upd",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint")
                                ),
                                requestFields(
                                        subsectionWithPath("*").description(editableStateDescription)
                                )));

        mockMvc.perform(get(Programs.COLLECTION, ENDPOINT_ID))
                .andExpect(status().isOk())
                .andDo(
                        document("program-list",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint")
                                ),
                                responseFields(
                                        subsectionWithPath("[]").description("List of program objects")
                                )));

        mockMvc.perform(get(Programs.ITEM, ENDPOINT_ID, PROGRAM_ID))
                .andExpect(status().isOk())
                .andDo(
                        document("program-item",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint"),
                                        parameterWithName(Ids.PROGRAM).description("Identifier of a program")
                                ),
                                responseFields(
                                        subsectionWithPath("id").description("Identifier of the program"),
                                        subsectionWithPath("name").description("Short name of the program"),
                                        subsectionWithPath("description").description("Description of the program"),
                                        subsectionWithPath("parameterScheme").description("Json schema defining structure of parameters the program accepts")
                                )));

        String createdLocation = mockMvc.perform(
                post(Jobs.COLLECTION, ENDPOINT_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(getJob()))
                .andExpect(status().isCreated())
                .andDo(
                        document("job-create",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint")
                                ),
                                requestFields(
                                        subsectionWithPath("program").description("Program ID for the job"),
                                        subsectionWithPath("params").description("Parameters for the program"),
                                        subsectionWithPath("expectedToStart").description("Optional parameter defining time when the job should be started")
                                ),
                                responseHeaders(
                                        headerWithName(LOCATION).description("URI of created object")
                                )))
                .andReturn().getResponse().getHeader(LOCATION);

        String createdId = createdLocation.substring(createdLocation.lastIndexOf("/") + 1);

        mockMvc.perform(
                put(Jobs.ITEM, ENDPOINT_ID, createdId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(getJob()))
                .andExpect(status().is(204))
                .andDo(
                        document("job-upd",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint"),
                                        parameterWithName(Ids.JOB).description("Id of a job")
                                ),
                                requestFields(
                                        subsectionWithPath("program").description("Program ID for the job"),
                                        subsectionWithPath("params").description("Parameters for the program"),
                                        subsectionWithPath("expectedToStart").description("Optional parameter defining time when the job should be started")
                                ),
                                responseHeaders(
                                        headerWithName(LOCATION).description("URI of updated object")
                                )));


        mockMvc.perform(
                get(Jobs.ITEM, ENDPOINT_ID, createdId))
                .andExpect(status().isOk())
                .andDo(
                        document("job-get",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint"),
                                        parameterWithName(Ids.JOB).description("Id of a job")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("Job ID"),
                                        fieldWithPath("program").description("Program for the job"),
                                        subsectionWithPath("params").description("Parameters for the program"),
                                        fieldWithPath("expectedToStart").description("Time when the job was expected to be started"),
                                        fieldWithPath("expectedToFinish").description("Time when the job was expected to be finished"),
                                        fieldWithPath("finished").description("Time when the job actually started"),
                                        fieldWithPath("started").description("Time when the job actually finished")
                                )));

        mockMvc.perform(
                get(Jobs.COLLECTION, ENDPOINT_ID))
                .andExpect(status().isOk())
                .andDo(
                        document("job-list",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint")
                                ),
                                responseFields(
                                        subsectionWithPath("[]").description("List of jobs waiting to be completed")
                                )));


        mockMvc.perform(
                delete(Jobs.ITEM, ENDPOINT_ID, createdId))
                .andExpect(status().is(204))
                .andDo(
                        document("job-delete",
                                pathParameters(
                                        parameterWithName(Ids.ENDPOINT).description("Identifier (name) of an endpoint"),
                                        parameterWithName(Ids.JOB).description("Id of a job")
                                )));
    }

    private String getJob() throws JsonProcessingException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("sound", "off");
        JobMainView job = new JobMainView();
        job.setProgram(PROGRAM_ID);
        job.setParams(params);
        job.setExpectedToStart("2018-10-31T00:22:22.001+03:00[Europe/Moscow]");
        return MAPPER.writeValueAsString(job);
    }
}
