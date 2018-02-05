package gtests.appliances.presentation.controller;

import gtests.appliances.validation.JsonSchemaValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for processing json validation exceptions
 *
 * @author g-tests
 */
@ControllerAdvice
public class ValidationFailureAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonSchemaValidationException.class)
    public void handleValidationFailure(JsonSchemaValidationException e) {

    }
}
