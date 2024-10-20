package de.marcelgerber.springboard.core.chesslogic;

import de.marcelgerber.springboard.core.chesslogic.pieces.NullPiece;
import de.marcelgerber.springboard.core.chesslogic.pieces.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Stack;

/**
 * Class for representing a logical (not persistent) Chess board.
 */
@Getter
public class Board {

    @Data
    @AllArgsConstructor
    private class StateInfo {
        private Castling castling;
        private Square enPassant;
        private Piece captured;
    }

    private Piece[] pieces;
    private Color sideToMove;
    private Castling castling;
    private Square enPassant;

    // All previous states of the board will be saved in here
    private final Stack<StateInfo> prevStates = new Stack<>();

    public Board() {
        pieces = new Piece[64];
        sideToMove = Color.WHITE;
        castling = new Castling();
        enPassant = new Square(SquareValue.NONE);
    }

    /**
     * Returns the piece standing on this index
     *
     * @param index Index of square
     * @return Piece
     */
    public Piece getPiece(final byte index) {
        return pieces[index];
    }

    /**
     * Returns the piece standing on this square
     *
     * @param square Square
     * @return Piece
     */
    public Piece getPiece(final Square square) {
        return pieces[square.getIndex()];
    }

    /**
     * Returns the piece standing on this square, but checks if the square is valid.
     * If not it returns the NullPieces' instance
     *
     * @param square Square
     * @return Piece
     */
    public Piece getPieceOrNullPiece(Square square) {
        if(square.getValue() == SquareValue.NONE) {
            return NullPiece.getInstance();
        }
        return getPiece(square.getIndex());
    }

    /**
     * Places a piece on the board
     *
     * @param index Index of square
     * @param piece Piece
     */
    private void placePiece(byte index, Piece piece) {
        pieces[index] = piece;
    }

    /**
     * Removes a piece from the board
     *
     * @param index Index of square
     */
    private void removePiece(byte index) {
        pieces[index] = NullPiece.getInstance();
    }

    /**
     * Returns 'true' if the piece standing on the index is a NullPiece
     *
     * @param index Index of square
     * @return boolean
     */
    public boolean isEmpty(byte index) {
        return pieces[index] instanceof NullPiece;
    }

    /**
     * Returns 'true' if the piece standing on the index is a NullPiece
     *
     * @param square Square
     * @return boolean
     */
    public boolean isEmpty(Square square) {
        return pieces[square.getIndex()] instanceof NullPiece;
    }

    /**
     * Returns 'true' if all indices of the array are empty on the board
     *
     * @param indices Indices of squares
     * @return boolean
     */
    public boolean areEmpty(byte[] indices) {
        for(byte index : indices) {
            if(!isEmpty(index)) return false;
        }
        return true;
    }

    /**
     * Returns 'true' if the piece standing on the provided square has the same color as the provided piece
     *
     * @param square Square
     * @param piece Piece
     * @return boolean
     */
    public boolean isFriendly(Square square, Piece piece) {
        return getPiece(square).getColor() == piece.getColor();
    }

    /**
     * Returns 'true' if the piece standing on the provided square has the opposite color as the provided piece
     *
     * @param square Square
     * @param piece Piece
     * @return boolean
     */
    public boolean isOpponent(Square square, Piece piece) {
        Piece targetPiece = getPiece(square);
        return (!(targetPiece instanceof NullPiece) && targetPiece.getColor() != piece.getColor());
    }

    /**
     * Returns 'true' if the piece standing on the provided square is a NullPiece
     * or the provided piece is an enemy piece
     *
     * @param square Square
     * @param piece Piece
     * @return boolean
     */
    public boolean isEmptyOrOpponent(Square square, Piece piece) {
        Piece targetPiece = getPiece(square);
        return targetPiece instanceof NullPiece || targetPiece.getColor() != piece.getColor();
    }

}
