package de.marcelgerber.springboard.exceptions;

/**
 * Exception is thrown when a player wants to join a game that's not waiting for a player to join or
 * when a player wants to play a move when the game is not in 'ONGOING' state
 */
public class IllegalGameStateException extends RuntimeException {

    public IllegalGameStateException(String message) {
        super(message);
    }

}
