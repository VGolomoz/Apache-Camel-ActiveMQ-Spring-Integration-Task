package app.integration.exceptions;

/**
 * Exception which throws when processing undefined file type.
 *
 * @author Vadym
 */
public class UndefinedFileTypeException extends Exception {

    /**
     * Constructs a new UndefinedFileTypeException with null as its detail message.
     */
    public UndefinedFileTypeException() {
    }

    /**
     * Constructs a new UndefinedFileTypeException with the specified detail message.
     */
    public UndefinedFileTypeException(String message) {
        super(message);
    }
}
