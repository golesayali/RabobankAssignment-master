package nl.rabobank.exception;

/**
 * This exception will be thrown whenever system behaves differently.
 *
 * @author Sayali G
 */
public class AccountAuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Account authorization exception.
     *
     * @param message the error message
     */
    public AccountAuthorizationException(String message) {
        super(message);
    }
}
