package de.marcelgerber.springboard.util.chesslogic.pieces;

import de.marcelgerber.springboard.util.chesslogic.Board;
import de.marcelgerber.springboard.util.chesslogic.Color;
import de.marcelgerber.springboard.util.chesslogic.Move;
import de.marcelgerber.springboard.util.chesslogic.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bishop in a chess game
 */
public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Square from) {
        List<Move> legalMoves = new ArrayList<>();

        for(Square to : super.getBishopAttacks(board, from, board::isKing)) {
            legalMoves.add(new Move(from, to));
        }

        return legalMoves;
    }

    @Override
    public List<Square> getAttackedSquares(Board board, Square from) {
        return super.getBishopAttacks(board, from, square -> false);
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }

    @Override
    public char getChar() {
        return this.getColor() == Color.WHITE ? 'B' : 'b';
    }
}
