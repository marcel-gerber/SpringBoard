package de.marcelgerber.springboard.exceptions;

/**
 * Exception is thrown when a Player with a specific playerId couldn't be found
 */
public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String playerId) {
        super("Player not found with ID: " + playerId);
    }

}
