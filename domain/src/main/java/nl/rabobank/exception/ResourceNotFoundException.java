package nl.rabobank.exception;

/**
 * This exception will be thrown whenever service does not find a resource.
 *
 * @author Sayali G
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 869826560650856941L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
