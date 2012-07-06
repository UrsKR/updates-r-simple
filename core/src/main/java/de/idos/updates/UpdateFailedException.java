package de.idos.updates;

public class UpdateFailedException extends RuntimeException{
    public UpdateFailedException(String message, Exception cause) {
        super(message, cause);
    }
}