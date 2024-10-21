package de.marcelgerber.springboard.core.chesslogic.pieces;

import de.marcelgerber.springboard.core.chesslogic.Board;
import de.marcelgerber.springboard.core.chesslogic.Color;
import de.marcelgerber.springboard.core.chesslogic.Move;
import de.marcelgerber.springboard.core.chesslogic.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a queen in a chess game
 */
public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Square from) {
        List<Move> legalMoves = new ArrayList<>();

        for(Square to : super.getBishopAttacks(board, from, board::isKing)) {
            legalMoves.add(new Move(from, to));
        }

        for(Square to : super.getRookAttacks(board, from, board::isKing)) {
            legalMoves.add(new Move(from, to));
        }

        return legalMoves;
    }

    @Override
    public List<Square> getAttackedSquares(Board board, Square from) {
        List<Square> attackedSquares = super.getRookAttacks(board, from, square -> false);
        attackedSquares.addAll(super.getBishopAttacks(board, from, square -> false));
        return attackedSquares;
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }

    @Override
    public char getChar() {
        return this.getColor() == Color.WHITE ? 'Q' : 'q';
    }
}
