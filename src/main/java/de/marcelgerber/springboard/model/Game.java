package de.marcelgerber.springboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.marcelgerber.springboard.exception.BadRequestException;
import de.marcelgerber.springboard.util.chesslogic.GameState;
import de.marcelgerber.springboard.util.chesslogic.*;
import de.marcelgerber.springboard.util.chesslogic.pieces.Piece;
import de.marcelgerber.springboard.util.chesslogic.pieces.PieceType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    @DBRef
    private Player playerWhite = null;

    @DBRef
    private Player playerBlack = null;

    private ArrayList<String> moves;

    @Transient
    @JsonIgnore
    private Board board;

    protected Game() { }

    public Game(Color color, Player player) {
        this.fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        this.state = GameState.WAITING_FOR_PLAYER_TO_JOIN;
        this.moves = new ArrayList<>();

        if(color == Color.WHITE) {
            this.playerWhite = player;
        } else {
            this.playerBlack = player;
        }
    }

    /**
     * Plays the move on the board and updates the Game
     *
     * @param sMove String-move
     */
    public void playMove(String sMove) {
        // Convert String-move to Move
        Move move = convertMove(sMove);
        if(move == null) throw new BadRequestException("Could not convert move: " + sMove);
        if(!isMoveLegal(move)) throw new BadRequestException("Illegal move: " + move.toPureCoordinateNotation());

        // Make move on board and update Games' FEN-String and moves-list
        this.board.makeMove(move);
        this.fen = this.board.getFen();
        this.moves.add(move.toPureCoordinateNotation());
    }

    /**
     * Converts a String-move into a Move-Object
     *
     * @param move String-move
     * @return Move
     */
    private Move convertMove(String move) {
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
            default -> throw new BadRequestException("Unexpected length of move: " + move.length());
        }
        return null;
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
     * Sets the player who is joining the game
     *
     * @param player Player
     */
    public void setJoiningPlayerName(Player player) {
        if(playerWhite == null) {
            this.playerWhite = player;
        } else {
            this.playerBlack = player;
        }
    }

    /**
     * Sets the GameState to 'ONGOING'
     */
    public void setOngoing() {
        this.state = GameState.ONGOING;
    }

    /**
     * Returns the player who is currently waiting for another player to join
     *
     * @return Player
     */
    @JsonIgnore
    public Player getWaitingPlayer() {
        if(playerWhite == null && playerBlack != null) return playerBlack;
        if(playerBlack == null && playerWhite != null) return playerWhite;
        return null;
    }

    /**
     * Returns the player who is next to move
     *
     * @return Player
     */
    @JsonIgnore
    public Player getPlayerToMove() {
        return switch(this.board.getSideToMove()) {
            case WHITE -> playerWhite;
            case BLACK -> playerBlack;
            case NONE -> throw new IllegalStateException("Problem in getPlayerToMove(): SideToMove is NONE");
        };
    }

    /**
     * Initializes the internal Board
     */
    public void initializeBoard() {
        if(this.board != null) return;

        this.board = new Board();
        this.board.setFen(this.fen);
    }

}
