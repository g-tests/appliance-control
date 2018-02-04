package gtests.appliances.validation;

import com.github.fge.jsonschema.core.report.ProcessingReport;

/**
 * Exception thrown when schema validation fails
 *
 * @author g-tests
 */
public class JsonSchemaValidationException extends RuntimeException {

    private ProcessingReport report;

    public JsonSchemaValidationException(Throwable cause) {
        super(cause);
    }

    public JsonSchemaValidationException(ProcessingReport report) {

        this.report = report;
    }

    public ProcessingReport getReport() {
        return report;
    }
}
