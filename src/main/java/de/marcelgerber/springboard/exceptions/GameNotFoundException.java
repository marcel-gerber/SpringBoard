package de.marcelgerber.springboard.exceptions;

/**
 * Exception is thrown when a Game with a specific gameId couldn't be found
 */
public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String gameId) {
        super("Game not found with ID: " + gameId);
    }

}
