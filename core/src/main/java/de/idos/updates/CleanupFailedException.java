package de.idos.updates;

public class CleanupFailedException extends RuntimeException {
    public CleanupFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
