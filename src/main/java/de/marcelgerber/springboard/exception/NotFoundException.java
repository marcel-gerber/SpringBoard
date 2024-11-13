package de.marcelgerber.springboard.exception;

/**
 * Exception is thrown when a Game/Player with a specific ID couldn't be found
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}
