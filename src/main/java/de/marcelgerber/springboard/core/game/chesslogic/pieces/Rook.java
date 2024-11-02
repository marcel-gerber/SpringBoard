package de.marcelgerber.springboard.core.game.chesslogic.pieces;

import de.marcelgerber.springboard.core.game.chesslogic.Board;
import de.marcelgerber.springboard.core.game.chesslogic.Color;
import de.marcelgerber.springboard.core.game.chesslogic.Move;
import de.marcelgerber.springboard.core.game.chesslogic.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rook in a chess game
 */
public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Square from) {
        List<Move> legalMoves = new ArrayList<>();

        for(Square to : super.getRookAttacks(board, from, board::isKing)) {
            legalMoves.add(new Move(from, to));
        }

        return legalMoves;
    }

    @Override
    public List<Square> getAttackedSquares(Board board, Square from) {
        return super.getRookAttacks(board, from, square -> false);
    }

    @Override
    public PieceType getType() {
        return PieceType.ROOK;
    }

    @Override
    public char getChar() {
        return this.getColor() == Color.WHITE ? 'R' : 'r';
    }
}
