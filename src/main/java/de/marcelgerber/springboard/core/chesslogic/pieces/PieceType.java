package de.marcelgerber.springboard.core.chesslogic.pieces;

import de.marcelgerber.springboard.core.chesslogic.Color;

/**
 * Enum containing all piece types of a chess game
 */
public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING,
    NONE;

    public Piece getPiece(Color color) {
        return switch (this) {
            case PieceType.KNIGHT -> new Knight(color);
            case PieceType.BISHOP -> new Bishop(color);
            case PieceType.ROOK -> new Rook(color);
            case PieceType.QUEEN -> new Queen(color);
            default -> NullPiece.getInstance();
        };
    }

    @Override
    public String toString() {
        return this.name();
    }
}
