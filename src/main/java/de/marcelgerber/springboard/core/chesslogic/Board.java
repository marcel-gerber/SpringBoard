package de.marcelgerber.springboard.core.chesslogic;

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
        enPassant = Square.NONE;
    }

}
