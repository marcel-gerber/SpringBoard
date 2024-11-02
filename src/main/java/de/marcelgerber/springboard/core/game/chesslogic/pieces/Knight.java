package de.marcelgerber.springboard.core.game.chesslogic.pieces;

import de.marcelgerber.springboard.core.game.chesslogic.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a knight in a chess game
 */
public class Knight extends Piece {

    private static final Direction[] legalDirections = { Direction.KNIGHT_NORTH_NORTH_WEST,
            Direction.KNIGHT_NORTH_NORTH_EAST, Direction.KNIGHT_NORTH_EAST_EAST, Direction.KNIGHT_SOUTH_EAST_EAST,
            Direction.KNIGHT_SOUTH_SOUTH_EAST, Direction.KNIGHT_SOUTH_SOUTH_WEST, Direction.KNIGHT_SOUTH_WEST_WEST,
            Direction.KNIGHT_NORTH_WEST_WEST
    };

    public Knight(Color color) {
        super(color);
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Square from) {
        List<Move> legalMoves = new ArrayList<>();

        for(Direction direction : legalDirections) {
            Square to = Square.add(from, direction);

            if(to.getValue() == SquareValue.NONE) continue;

            if(board.isEmptyOrOpponent(to, this) && !board.isKing(to)) {
                legalMoves.add(new Move(from, to));
            }
        }
        return legalMoves;
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
        return PieceType.KNIGHT;
    }

    @Override
    public char getChar() {
        return this.getColor() == Color.WHITE ? 'N' : 'n';
    }
}
