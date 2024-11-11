package de.marcelgerber.springboard.exceptions;

/**
 * Exception is thrown when someone wants to register a new player with a username that's already taken
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("Username is already taken: " + username);
    }

}
