package nl.rabobank.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

/**
 * This ExceptionResponse wrap exception and http status.
 *
 * @author Sayali G
 */
@Value
public class ExceptionResponse {

    HttpStatus status;
    String message;

    /**
     * Instantiates a new Exception response.
     *
     * @param message the message
     * @param status  the status
     */
    public ExceptionResponse(String message, HttpStatus status) {
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
