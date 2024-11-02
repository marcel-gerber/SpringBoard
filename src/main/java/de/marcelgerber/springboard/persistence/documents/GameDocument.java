package de.marcelgerber.springboard.persistence.documents;

import de.marcelgerber.springboard.core.game.GameState;
import de.marcelgerber.springboard.core.game.chesslogic.Color;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * Represents a chess game in a MongoDB database
 */
@Document("games")
@Data
public class GameDocument {

    @Id
    private String id;
    private String fen;
    private GameState state;
    private String playerWhite = null;
    private String playerBlack = null;
    private ArrayList<String> moves;

    protected GameDocument() { }

    public GameDocument(Color color, String nickname) {
        this.fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        this.state = GameState.WAITING_FOR_PLAYER_TO_JOIN;
        this.moves = new ArrayList<>();

        if(color == Color.WHITE) {
            this.playerWhite = nickname;
        } else {
            this.playerBlack = nickname;
        }
    }

}
