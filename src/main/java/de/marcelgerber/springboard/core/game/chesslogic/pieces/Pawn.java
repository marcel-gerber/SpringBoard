package de.marcelgerber.springboard.core.game.chesslogic.pieces;

import de.marcelgerber.springboard.core.chesslogic.*;
import de.marcelgerber.springboard.core.game.chesslogic.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    private static final Direction[] whiteAttacks = { Direction.NORTH_EAST, Direction.NORTH_WEST };
    private static final Direction[] blackAttacks = { Direction.SOUTH_EAST, Direction.SOUTH_WEST };

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Square from) {
        List<Move> pseudoLegalMoves = new ArrayList<>();
        Color color = getColor();

        Direction push = color == Color.WHITE ? Direction.NORTH : Direction.SOUTH;
        Square to = Square.add(from, push);

        // Single-Push and Double-Push
        if(board.isEmpty(to)) {
            if(isOnPromotionRank(color, to)) {
                addPromotionMoves(pseudoLegalMoves, from, to);
            }
            else {
                pseudoLegalMoves.add(new Move(from, to));

                if(isOnDoublePushRank(color, from)) {
                    Square doublePush = Square.add(to, push);

                    if(board.isEmpty(doublePush)) {
                        pseudoLegalMoves.add(new Move(from, doublePush));
                    }
                }
            }
        }

        // Attacks left and right
        for(Direction attack : getAttackDirections(color)) {
            Square attackedSquare = Square.add(from, attack);

            if(attackedSquare.getValue() == SquareValue.NONE) continue;

            if(attackedSquare.getIndex() == board.getEnPassant().getIndex()) {
                pseudoLegalMoves.add(new Move(MoveType.ENPASSANT, from, attackedSquare));
                continue;
            }

            if(board.isOpponent(attackedSquare, this) && !board.isKing(attackedSquare)) {
                if(isOnPromotionRank(color, attackedSquare)) {
                    addPromotionMoves(pseudoLegalMoves, from, attackedSquare);
                }
                else {
                    pseudoLegalMoves.add(new Move(from, attackedSquare));
                }
            }
        }
        return pseudoLegalMoves;
    }

    private void addPromotionMoves(List<Move> moves, Square from, Square to) {
        moves.add(new Move(MoveType.PROMOTION, from, to, PieceType.KNIGHT));
        moves.add(new Move(MoveType.PROMOTION, from, to, PieceType.BISHOP));
        moves.add(new Move(MoveType.PROMOTION, from, to, PieceType.ROOK));
        moves.add(new Move(MoveType.PROMOTION, from, to, PieceType.QUEEN));
    }

    @Override
    public List<Square> getAttackedSquares(Board board, Square from) {
        List<Square> attackedSquares = new ArrayList<>();

        for(Direction attack : getAttackDirections(getColor())) {
            Square attackedSquare = Square.add(from, attack);

            if(attackedSquare.getValue() == SquareValue.NONE) continue;

            if(board.isEmptyOrOpponent(attackedSquare, this)) {
                attackedSquares.add(attackedSquare);
            }
        }
        return attackedSquares;
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    @Override
    public char getChar() {
        return this.getColor() == Color.WHITE ? 'P' : 'p';
    }

    /**
     * Returns 'true' if the pawn is on the double push rank
     *
     * @param color Color
     * @param from Square
     * @return boolean
     */
    private static boolean isOnDoublePushRank(Color color, Square from) {
        return switch (color) {
            case Color.WHITE -> from.getRankIndex() == 1;
            case Color.BLACK -> from.getRankIndex() == 6;
            default -> false;
        };
    }

    /**
     * Returns 'true' if the targeted square (that the pawn moves to) is the promotion rank
     *
     * @param color Color
     * @param to targeted Square
     * @return boolean
     */
    private static boolean isOnPromotionRank(Color color, Square to) {
        return switch (color) {
            case Color.WHITE -> to.getRankIndex() == 7;
            case Color.BLACK -> to.getRankIndex() == 0;
            default -> false;
        };
    }

    /**
     * Returns a pawns attacks based on the provided color
     *
     * @param color Color
     * @return Array of Directions
     */
    private static Direction[] getAttackDirections(Color color) {
        return switch (color) {
            case Color.WHITE -> whiteAttacks;
            case Color.BLACK -> blackAttacks;
            default -> throw new IllegalStateException("Unexpected value: " + color);
        };
    }
}
