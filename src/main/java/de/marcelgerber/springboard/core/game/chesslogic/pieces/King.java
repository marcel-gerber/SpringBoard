package de.marcelgerber.springboard.core.game.chesslogic.pieces;

import de.marcelgerber.springboard.core.game.chesslogic.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a king in a chess game
 */
public class King extends Piece {

    private static final Direction[] legalDirections = { Direction.NORTH, Direction.NORTH_EAST, Direction.EAST,
            Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST
    };

    public King(Color color) {
        super(color);
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Square from) {
        List<Move> legalMoves = new ArrayList<>();

        for(Direction direction : legalDirections) {
            Square to = Square.add(from, direction);

            if(to.getValue() == SquareValue.NONE) continue;

            if(board.isEmptyOrOpponent(to, this)) {
                legalMoves.add(new Move(from, to));
            }
        }

        addCastlingMoves(legalMoves, board, from);
        return legalMoves;
    }

    private void addCastlingMoves(List<Move> pseudoLegalMoves, Board board, Square from) {
        if(board.getCastling().hasNoCastling()) return;

        CastlingValue[] castlings = Castling.getCastlings(this.getColor());

        for(CastlingValue castlingValue : castlings) {
            if(board.getCastling().has(castlingValue)) {
                byte[] emptySquares = Castling.getEmptySquares(castlingValue);
                byte[] notAttackedSquares = Castling.getNotAttackedSquares(castlingValue);

                if(!board.areEmpty(emptySquares)) continue;
                if(board.areAttacked(notAttackedSquares)) continue;

                byte targetKindIndex = Castling.getKingTargetIndex(castlingValue);
                Square targetSquare = new Square(targetKindIndex);

                pseudoLegalMoves.add(new Move(MoveType.CASTLING, from, targetSquare));
            }
        }
    }

    @Override
    public List<Square> getAttackedSquares(Board board, Square from) {
        List<Square> attackedSquares = new ArrayList<>();

        for(Direction direction : legalDirections) {
            Square to = Square.add(from, direction);

            if(to.getValue() == SquareValue.NONE) continue;

            if(board.isEmptyOrOpponent(to, this)) {
                attackedSquares.add(to);
            }
        }
        return attackedSquares;
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }

    @Override
    public char getChar() {
        return this.getColor() == Color.WHITE ? 'K' : 'k';
    }
}
