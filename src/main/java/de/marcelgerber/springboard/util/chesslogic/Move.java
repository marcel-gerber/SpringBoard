package de.marcelgerber.springboard.util.chesslogic;

import de.marcelgerber.springboard.util.chesslogic.pieces.PieceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representing a move in a chess game
 */
@Getter
@AllArgsConstructor
public class Move {

    private final MoveType moveType;
    private final Square from;
    private final Square to;
    private final PieceType promotion;

    public Move(Square from, Square to) {
        this.moveType = MoveType.NORMAL;
        this.from = from;
        this.to = to;
        this.promotion = PieceType.NONE;
    }

    public Move(MoveType moveType, Square from, Square to) {
        this.moveType = moveType;
        this.from = from;
        this.to = to;
        this.promotion = PieceType.NONE;
    }

    /**
     * Converts the move to a "Pure Coordinate Notation"-String
     *
     * @return String in Pure Coordinate Notation style
     */
    public String toPureCoordinateNotation() {
        if(promotion == PieceType.NONE) {
            return from.toString() + to.toString();
        }
        return from.toString() + to.toString() + promotion.getChar();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Move move)) return false;
        return moveType == move.moveType
                && from.getIndex() == move.from.getIndex()
                && to.getIndex() == move.to.getIndex()
                && promotion == move.promotion;
    }

    @Override
    public String toString() {
        return from.toString() + to.toString() + " MoveType: " + moveType.toString() + " Promotion: " + promotion.toString();
    }
}
