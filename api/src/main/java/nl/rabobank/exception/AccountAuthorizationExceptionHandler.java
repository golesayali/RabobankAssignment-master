package nl.rabobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler prepares error response based on the various
 * exception handler class.
 *
 * @author Sayali G
 */
@ControllerAdvice
public class AccountAuthorizationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountAuthorizationException.class)
    public final ResponseEntity<ExceptionResponse> handleAccountAuthorizationException(final AccountAuthorizationException e) {
        ExceptionResponse response = ExceptionResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(nl.rabobank.exception.ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(final nl.rabobank.exception.ResourceNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
