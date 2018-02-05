package gtests.appliances.test.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static gtests.appliances.presentation.ResourcePaths.Endpoints;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for endpoint's editable state controller
 *
 * @author g-tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EditableStateControllerTest extends AbstractControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldGetState() throws Exception {
        MvcResult result = mockMvc.perform(
                get(Endpoints.EDITABLE_STATE, preloadedEndpoint.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        byte[] responseState = result.getResponse().getContentAsByteArray();
        Map<String, Object> resultMap =
                objectMapper.readValue(responseState, new TypeReference<Map<String, Object>>() {
                });
        assertEquals(preloadedEndpoint.getEditableState(), resultMap);
    }

    @Test
    public void shouldPutState() throws Exception {
        Map<String, Object> originalState = preloadedEndpoint.getEditableState();
        HashMap<String, Object> newState = new HashMap<>(originalState);
        String stateKey = "sound";
        String value = (String) newState.get(stateKey);
        // this test has to hardcode some data, so check these assumptions
        assertNotNull(value);
        assertThat(value, isOneOf("on", "off"));
        newState.put(stateKey, "on".equals(value) ? "off" : "on");


        // see if put with new state succeeds
        mockMvc.perform(
                put(Endpoints.EDITABLE_STATE, preloadedEndpoint.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(newState)))
                .andExpect(status().is(204))
                .andReturn();

        // check that update gets read
        MvcResult result = mockMvc.perform(get(Endpoints.EDITABLE_STATE, preloadedEndpoint.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        byte[] responseState = result.getResponse().getContentAsByteArray();
        Map<String, Object> resultMap =
                objectMapper.readValue(responseState, new TypeReference<Map<String, Object>>() {
                });

        assertNotEquals(originalState, resultMap);
        assertEquals(newState, resultMap);
    }

    @Test
    public void shouldNotGetState() throws Exception {
        mockMvc.perform(get(Endpoints.EDITABLE_STATE, "nonExistentEndpoint"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotPutState() throws Exception {
        mockMvc.perform(put(Endpoints.EDITABLE_STATE, "nonExistentEndpoint")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

}
