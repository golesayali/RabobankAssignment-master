package nl.rabobank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * This ExceptionResponse wrap exception and http status.
 *
 * @author Sayali G
 */
@Getter
public class ExceptionResponse {

    private final HttpStatus status;
    private final String message;

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
