package gtests.appliances.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Utility to validate objects against json-schema
 *
 * @author g-tests
 */
public class JsonValidationUtil {

    public static final JsonSchemaFactory SCHEMA_FACTORY = JsonSchemaFactory.byDefault();

    public static void validateMap(Map<String, Object> params, String scheme) {
        try {
            ProcessingReport processingMessages = validateWithString(params, scheme);
            if (!processingMessages.isSuccess()) {
                throw new JsonSchemaValidationException(processingMessages);
            }
        } catch (IOException | ProcessingException e) {
            throw new JsonSchemaValidationException(e);
        }
    }

    private static ProcessingReport validateWithString(Map<String, Object> params, String scheme) throws IOException, ProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schemaNode = mapper.readTree(scheme);
        JsonNode paramNode = mapper.valueToTree(params);
        JsonSchema jsonSchema = SCHEMA_FACTORY.getJsonSchema(schemaNode);
        return jsonSchema.validate(paramNode);
    }
}
