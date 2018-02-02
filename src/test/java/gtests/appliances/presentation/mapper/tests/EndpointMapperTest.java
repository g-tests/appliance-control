package gtests.appliances.presentation.mapper.tests;

import gtests.appliances.persistence.model.Endpoint;
import gtests.appliances.presentation.view.EndpointMainView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static gtests.appliances.presentation.mapper.EndpointMapper.ENDPOINT_MAPPER;
import static gtests.appliances.test.util.EntityDataProvider.getFullEndpoint;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests conversion between entities and views for endpoints
 *
 * @author g-tests
 */
public class EndpointMapperTest {

    public static final String DIRECTORY_NAME = "oven";

    private Endpoint endpoint;

    @Before
    public void setUp() throws IOException {
        endpoint = getFullEndpoint(DIRECTORY_NAME);
    }

    @After
    public void cleanUp() {
        endpoint = null;
    }

    @Test
    public void testEntityToViewMapping() {
        EndpointMainView resource = ENDPOINT_MAPPER.entityToView(endpoint);
        assertEquals(endpoint.getId(), resource.getName());
        assertEquals(endpoint.getType(), resource.getType());
        assertFalse("Maps must not be the same", endpoint.getEditableState() == resource.getEditableState());
        assertEquals(endpoint.getEditableState(), resource.getEditableState());
    }
}
