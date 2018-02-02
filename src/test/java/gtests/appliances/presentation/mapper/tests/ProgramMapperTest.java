package gtests.appliances.presentation.mapper.tests;

import gtests.appliances.persistence.model.EndpointProgram;
import gtests.appliances.presentation.view.ProgramMainView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static gtests.appliances.presentation.mapper.ProgramMapper.PROGRAM_MAPPER;
import static gtests.appliances.test.util.EntityDataProvider.getFullEndpoint;
import static org.junit.Assert.assertEquals;

/**
 * Tests conversion between entities and views for programs
 *
 * @author g-tests
 */
public class ProgramMapperTest {

    public static final String DIRECTORY_NAME = "oven";

    private EndpointProgram programEntity;

    @Before
    public void setUp() throws IOException {
        programEntity = getFullEndpoint(DIRECTORY_NAME)
                .getAvailablePrograms()
                .iterator()
                .next();
        programEntity.setId(123L);
    }

    @After
    public void cleanUp() {
        programEntity = null;
    }

    @Test
    public void testEntityToViewMapping() {
        ProgramMainView view = PROGRAM_MAPPER.entityToView(programEntity);
        assertCorrectConversion(view, programEntity);
    }

    public static void assertCorrectConversion(ProgramMainView view, EndpointProgram entity) {
        assertEquals(entity.getId(), view.getId());
        assertEquals(entity.getName(), view.getName());
        assertEquals(entity.getDescription(), view.getDescription());
        assertEquals(entity.getParameterScheme(), view.getParameterScheme());
    }
}
