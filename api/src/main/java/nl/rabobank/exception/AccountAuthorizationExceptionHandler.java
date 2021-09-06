package nl.rabobank.exception;

import nl.rabobank.dto.ExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler prepares error response based on the various
 * exception handler class.
 *
 * @author Sayali G
 */
@ControllerAdvice
public class AccountAuthorizationExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle all uncaught exception response entity.
     *
     * @param e the exception object
     * @return the response entity
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleAllUncaughtException(ResourceNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Handle account authorization exception response entity.
     *
     * @param e the exception object
     * @return the response entity
     */
    @ExceptionHandler(AccountAuthorizationException.class)
    public final ResponseEntity<ExceptionResponse> handleAccountAuthorizationException(final AccountAuthorizationException e) {
        ExceptionResponse response = ExceptionResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Handle bad request exception response entity.
     *
     * @param e the exception object
     * @return the response entity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(final nl.rabobank.exception.ResourceNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorMessage, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

}
