package de.marcelgerber.springboard.core.game.chesslogic.pieces;

import de.marcelgerber.springboard.core.game.chesslogic.Color;

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

    /**
     * Returns an instance of a Piece with the provided color
     *
     * @param color Color
     * @return Piece
     */
    public Piece getPiece(Color color) {
        return switch (this) {
            case PieceType.KNIGHT -> new Knight(color);
            case PieceType.BISHOP -> new Bishop(color);
            case PieceType.ROOK -> new Rook(color);
            case PieceType.QUEEN -> new Queen(color);
            default -> NullPiece.getInstance();
        };
    }

    /**
     * Returns the pieceTypes' character (used for determine promotion piece type)
     *
     * @return char
     */
    public char getChar() {
        return switch (this) {
            case PieceType.KNIGHT -> 'n';
            case PieceType.BISHOP -> 'b';
            case PieceType.ROOK -> 'r';
            case PieceType.QUEEN -> 'q';
            default -> ' ';
        };
    }

    @Override
    public String toString() {
        return this.name();
    }
}
