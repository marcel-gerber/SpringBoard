package de.marcelgerber.springboard.model;

import de.marcelgerber.springboard.exception.BadRequestException;
import de.marcelgerber.springboard.util.chesslogic.GameState;
import de.marcelgerber.springboard.util.chesslogic.*;
import de.marcelgerber.springboard.util.chesslogic.pieces.Piece;
import de.marcelgerber.springboard.util.chesslogic.pieces.PieceType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * Represents a chess game in a MongoDB database
 */
@Document("games")
@Data
public class Game {

    @Id
    private String id;
    private String fen;
    private GameState state;
    private String playerWhite = null;
    private String playerBlack = null;
    private ArrayList<String> moves;

    protected Game() { }

    public Game(Color color, String nickname) {
        this.fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        this.state = GameState.WAITING_FOR_PLAYER_TO_JOIN;
        this.moves = new ArrayList<>();

        if(color == Color.WHITE) {
            this.playerWhite = nickname;
        } else {
            this.playerBlack = nickname;
        }
    }

    /**
     * Plays the move on the board and updates the Game
     *
     * @param sMove String-move
     */
    public void playMove(String sMove) {
        // Convert current FEN-String to Board
        Board board = new Board();
        board.setFen(this.fen);

        // Convert String-move to Move
        Move move = convertMove(board, sMove);
        if(move == null) throw new BadRequestException("Could not convert move: " + sMove);
        if(!isMoveLegal(board, move)) throw new BadRequestException("Illegal move: " + move.toPureCoordinateNotation());

        // Make move on board and update Games' FEN-String and moves-list
        board.makeMove(move);
        this.fen = board.getFen();
        this.moves.add(move.toPureCoordinateNotation());
    }

    /**
     * Converts a String-move into a Move-Object
     *
     * @param board Board
     * @param move String-move
     * @return Move
     */
    private Move convertMove(Board board, String move) {
        if(move.length() < 4 || move.length() > 5) {
            throw new BadRequestException("Unexpected length of move: " + move.length());
        }

        String sFrom = move.substring(0, 2);
        String sTo = move.substring(2, 4);

        Square from = new Square(sFrom);
        Square to = new Square(sTo);

        if(from.getValue() == SquareValue.NONE || to.getValue() == SquareValue.NONE) {
            throw new BadRequestException("From or to Square out of range: " + move);
        }

        Piece piece = board.getPiece(from);

        // En Passant move
        if(piece.getType() == PieceType.PAWN && to.getIndex() == board.getEnPassant().getIndex()) {
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
            default -> throw new BadRequestException("Unexpected length of move: " + move.length());
        }
        return null;
    }

    /**
     * Returns 'true' when the provided move is a legal move
     *
     * @param board Board
     * @param move Move
     * @return boolean
     */
    private boolean isMoveLegal(Board board, Move move) {
        for(Move legal : board.getLegalMoves()) {
            if(move.equals(legal)) return true;
        }
        return false;
    }

    /**
     * Sets the players' name of the player who is joining the game
     *
     * @param playerName String
     */
    public void setJoiningPlayerName(String playerName) {
        if(playerWhite == null) {
            this.playerWhite = playerName;
        } else {
            this.playerBlack = playerName;
        }
    }

    /**
     * Sets the GameState to 'ONGOING'
     */
    public void setOngoing() {
        this.state = GameState.ONGOING;
    }

}
