package nl.rabobank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This ExceptionResponse wrap exception and http status.
 *
 * @author Sayali G
 */
@Getter
public class ExceptionResponse extends ResponseEntity {

    private final HttpStatus status;
    private final String message;

    /**
     * Instantiates a new Exception response.
     *
     * @param message the message
     * @param status  the status
     */
    public ExceptionResponse(String message, HttpStatus status) {
        super(message, status);
        this.status = status;
        this.message = message;
    }

    /**
     * This is static method which sets the exception
     *
     * @param message as error message
     * @param status  http status
     * @return ExceptionResponse pojo
     */
    public static ExceptionResponse of(final String message, HttpStatus status) {
        return new ExceptionResponse(message, status);
    }

}
