package gtests.appliances.presentation.controller;

import com.github.fge.jsonschema.core.report.ProcessingMessage;
import gtests.appliances.validation.JsonSchemaValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;

/**
 * Advice for processing json validation exceptions
 *
 * @author g-tests
 */
@ControllerAdvice
public class ValidationFailureAdvice {

    public static final String ERRORS_LIST_KEY = "errors";

    @ExceptionHandler(JsonSchemaValidationException.class)
    public ResponseEntity<?> handleValidationFailure(JsonSchemaValidationException e) {
        Map<String, List<String>> errors = StreamSupport.stream(e.getReport().spliterator(), false)
                .map(ProcessingMessage::getMessage)
                .collect(Collectors.groupingBy(key -> ERRORS_LIST_KEY));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleValidationFailure(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(
                singletonMap(ERRORS_LIST_KEY, singleton(e.getLocalizedMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationFailure(MethodArgumentNotValidException e) {
        Map<String, List<String>> errors =
                e.getBindingResult().getFieldErrors().stream()
                        .map(objectError ->
                                String.format("%s.%s: %s",
                                        objectError.getObjectName(),
                                        objectError.getField(),
                                        objectError.getDefaultMessage()))
                        .collect(Collectors.groupingBy(key -> ERRORS_LIST_KEY));
        return ResponseEntity.badRequest().body(errors);
    }
}
