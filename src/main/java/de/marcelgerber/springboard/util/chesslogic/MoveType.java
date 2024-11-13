package de.marcelgerber.springboard.util.chesslogic;

/**
 * Enum containing all different move types in chess
 */
public enum MoveType {
    NORMAL,
    CAPTURE,
    PROMOTION,
    ENPASSANT,
    CASTLING;

    @Override
    public String toString() {
        return this.name();
    }
}
