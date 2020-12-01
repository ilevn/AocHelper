package components;

/**
 * Unchecked class for {@code Input} exceptions.
 */
class InputException extends RuntimeException {
    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable cause) {
        super(message, cause);
    }
}
