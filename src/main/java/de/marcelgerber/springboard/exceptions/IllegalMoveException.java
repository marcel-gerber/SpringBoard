package de.marcelgerber.springboard.exceptions;

/**
 * Exception is thrown when an illegal move is present
 */
public class IllegalMoveException extends RuntimeException {

    public IllegalMoveException(String message) {
        super(message);
    }

}
