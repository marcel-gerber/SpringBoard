package de.marcelgerber.springboard.core;

import de.marcelgerber.springboard.core.chesslogic.Board;
import de.marcelgerber.springboard.core.chesslogic.Move;
import de.marcelgerber.springboard.core.chesslogic.MoveType;
import de.marcelgerber.springboard.core.chesslogic.Square;
import de.marcelgerber.springboard.core.chesslogic.pieces.Piece;
import de.marcelgerber.springboard.core.chesslogic.pieces.PieceType;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Class for representing a logical chess game
 */
@Getter
public class Game {

    @Getter(AccessLevel.NONE)
    private final Board board;

    private GameState gameState;
    private final String playerWhite;
    private final String playerBlack;
    private final ArrayList<Move> moves;

    public Game(String fen, GameState gameState, String playerWhite, String playerBlack, ArrayList<String> moves) {
        this.board = new Board();
        this.board.setStandardPosition();

        this.gameState = gameState;
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.moves = new ArrayList<>();

        convertAndPlayMoves(moves);

        if(!fen.equals(this.board.getFen())) {
            throw new IllegalStateException("Error when converting moves: Argument 'fen' does not match board fen");
        }
    }

    /**
     * Converts all String-moves to Move-Objects and plays each move on the board
     *
     * @param sMoves String-moves
     */
    private void convertAndPlayMoves(ArrayList<String> sMoves) {
        for(String sMove : sMoves) {
            Move move = convertMove(sMove);
            if(move == null) throw new IllegalArgumentException("Could not convert move: " + sMove);

            this.board.makeMove(move);
            this.moves.add(move);
        }
    }

    /**
     * Converts a String-move into a Move-Object
     *
     * @param move String-move
     * @return Move
     */
    private Move convertMove(String move) {
        String sFrom = move.substring(0, 2);
        String sTo = move.substring(2, 4);

        Square from = new Square(sFrom);
        Square to = new Square(sTo);
        Piece piece = this.board.getPiece(from);

        // En Passant move
        if(piece.getType() == PieceType.PAWN && to.getIndex() == this.board.getEnPassant().getIndex()) {
            return new Move(MoveType.ENPASSANT, from, to);
        }

        // Castling move
        if(piece.getType() == PieceType.KING && Math.abs(from.getIndex() - to.getIndex()) == 2) {
            return new Move(MoveType.CASTLING, from, to);
        }

        switch(move.length()) {
            case 4 -> {
                return new Move(from, to);
            }
            case 5 -> {
                Piece promotionPiece = Piece.fromChar(move.charAt(4));
                new Move(MoveType.PROMOTION, from, to, promotionPiece.getType());
            }
            default -> throw new IllegalStateException("Unexpected length of move: " + move.length());
        }
        return null;
    }

    /**
     * Plays the move on the board and adds it to the list
     *
     * @param sMove String-move
     */
    public void playMove(String sMove) {
        if(this.gameState != GameState.ONGOING) throw new IllegalStateException("Game is not in ongoing state");

        Move move = convertMove(sMove);
        if(move == null) throw new IllegalStateException("Could not convert move: " + sMove);
        if(!isMoveLegal(move)) throw new IllegalStateException("Illegal move: " + move);

        this.board.makeMove(move);
        this.moves.add(move);
    }

    /**
     * Returns 'true' when the provided move is a legal move
     *
     * @param move Move
     * @return boolean
     */
    private boolean isMoveLegal(Move move) {
        for(Move legal : this.board.getLegalMoves()) {
            if(move.equals(legal)) return true;
        }
        return false;
    }

    /**
     * Returns the moves as a list of string-moves
     *
     * @return List of string-moves
     */
    public ArrayList<String> getStringMoves() {
        ArrayList<String> stringMoves = new ArrayList<>();
        for(Move move : this.moves) {
            stringMoves.add(move.toPureCoordinateNotation());
        }
        return stringMoves;
    }

    /**
     * Returns the boards' fen string
     *
     * @return String
     */
    public String getFen() {
        return this.board.getFen();
    }

}
