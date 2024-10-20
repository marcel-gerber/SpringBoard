package de.marcelgerber.springboard.core.chesslogic.pieces;

import de.marcelgerber.springboard.core.chesslogic.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Abstract class representing a single Piece on the chess board
 */
public abstract class Piece {

    @Getter
    private final Color color;

    protected Piece(final Color color) {
        this.color = color;
    }

    /**
     * Calculates all <b>pseudo</b> legal moves
     *
     * @param board Board
     * @param from Square
     * @return List of Moves
     */
    public abstract List<Move> getPseudoLegalMoves(final Board board, final Square from);

    /**
     * Calculates all attacked squares
     *
     * @param board Board
     * @param from Square
     * @return List of Squares
     */
    public abstract List<Square> getAttackedSquares(final Board board, final Square from);

    /**
     * Adds an attack ray with the provided Direction to the squares List
     *
     * @param squares Square List
     * @param direction Direction
     * @param board Board
     * @param from Square
     * @param kingCheck Function to check whether there is a king on the current square
     */
    private void addAttackRay(List<Square> squares, final Direction direction, final Board board,
                             final Square from, Predicate<Square> kingCheck) {
        Square to = new Square(from);
        to.plus(direction);

        while(to.getValue() != SquareValue.NONE) {
            if(board.isFriendly(to, this) || kingCheck.test(to)) return;

            squares.add(to);

            if(board.isOpponent(to, this)) return;
            to.plus(direction);
        }
    }

    /**
     * Calculates a rooks attacks standing on the provided Square 'from'
     *
     * @param board Board
     * @param from Square
     * @param kingCheck Function to check whether there is a king on the current square
     * @return List of Squares
     */
    protected List<Square> getRookAttacks(Board board, Square from, Predicate<Square> kingCheck) {
        List<Square> attacks = new ArrayList<>();

        addAttackRay(attacks, Direction.NORTH, board, from, kingCheck);
        addAttackRay(attacks, Direction.EAST, board, from, kingCheck);
        addAttackRay(attacks, Direction.SOUTH, board, from, kingCheck);
        addAttackRay(attacks, Direction.WEST, board, from, kingCheck);

        return attacks;
    }

    /**
     * Calculates a bishops attacks standing on the provided Square 'from'
     *
     * @param board Board
     * @param from Square
     * @param kingCheck Function to check whether there is a king on the current square
     * @return List of Squares
     */
    protected List<Square> getBishopAttacks(Board board, Square from, Predicate<Square> kingCheck) {
        List<Square> attacks = new ArrayList<>();

        addAttackRay(attacks, Direction.NORTH_EAST, board, from, kingCheck);
        addAttackRay(attacks, Direction.SOUTH_EAST, board, from, kingCheck);
        addAttackRay(attacks, Direction.SOUTH_WEST, board, from, kingCheck);
        addAttackRay(attacks, Direction.NORTH_WEST, board, from, kingCheck);

        return attacks;
    }

    public abstract PieceType getType();

}
