package de.marcelgerber.springboard.core;

/**
 * Represents a current game state in chess
 */
public enum GameState {
    WAITING_FOR_PLAYER_TO_JOIN,
    ONGOING,
    DRAW_BY_50_MOVE_RULE,
    DRAW_BY_REPETITION,
    DRAW_BY_INSUFFICIENT_MATERIAL,
    WIN_BLACK,
    WIN_WHITE
}
