package de.marcelgerber.springboard.core.game.chesslogic.pieces;

import de.marcelgerber.springboard.core.game.chesslogic.Board;
import de.marcelgerber.springboard.core.game.chesslogic.Color;
import de.marcelgerber.springboard.core.game.chesslogic.Move;
import de.marcelgerber.springboard.core.game.chesslogic.Square;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Null Object pattern. Represents an emtpy square on the board
 */
public class NullPiece extends Piece {

    private static final List<Move> emptyMoves = new ArrayList<>();
    private static final List<Square> emptySquares = new ArrayList<>();

    @Getter
    private static final NullPiece instance = new NullPiece();

    private NullPiece() {
        super(Color.NONE);
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Square from) {
        return emptyMoves;
    }

    @Override
    public List<Square> getAttackedSquares(Board board, Square from) {
        return emptySquares;
    }

    @Override
    public PieceType getType() {
        return PieceType.NONE;
    }

    @Override
    public char getChar() {
        return ' ';
    }
}
