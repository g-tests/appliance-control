package gtests.appliances.test.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for endpoint's editable state controller
 *
 * @author g-tests
 */
public class EditableStateControllerTest extends AbstractControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldGetState() throws Exception {
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/state";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        byte[] responseState = result.getResponse().getContentAsByteArray();
        Map<String, Object> resultMap =
                objectMapper.readValue(responseState, new TypeReference<Map<String, Object>>() {
                });
        Assert.assertEquals(preloadedEndpoint.getEditableState(), resultMap);
    }

    @Test
    public void shouldPutState() throws Exception {
        String url = "/endpoints/" + preloadedEndpoint.getId() + "/state";
        Map<String, Object> originalState = preloadedEndpoint.getEditableState();
        Map<String, Object> testState = new HashMap<>();
        Map<String, Object> subMap = new HashMap<>();
        testState.put("alpha", "bravo");
        subMap.put("charlie", "delta");
        subMap.put("echo", "foxtrot");
        testState.put("golf", subMap);

        // see if put with new state succeeds
        mockMvc.perform(
                put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(testState)))
                .andExpect(status().is(204))
                .andReturn();

        // check that update gets read
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        byte[] responseState = result.getResponse().getContentAsByteArray();
        Map<String, Object> resultMap =
                objectMapper.readValue(responseState, new TypeReference<Map<String, Object>>() {
                });

        Assert.assertNotEquals(originalState, resultMap);
        Assert.assertEquals(testState, resultMap);
    }

    @Test
    public void shouldNotGetState() throws Exception {
        String url = "/endpoints/nonExistentEndpoint/state";
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotPutState() throws Exception {
        String url = "/endpoints/nonExistentEndpoint/state";
        mockMvc.perform(put(url)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}
