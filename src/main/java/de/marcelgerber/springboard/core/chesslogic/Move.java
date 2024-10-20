package de.marcelgerber.springboard.core.chesslogic;

import de.marcelgerber.springboard.core.chesslogic.pieces.PieceType;
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

}
