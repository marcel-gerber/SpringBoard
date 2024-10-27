package de.marcelgerber.springboard.persistence.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("games")
@Data
public class GameDocument {

    @Id
    private String id;
    private String fen;

    public GameDocument() {
        this.fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    }

}
