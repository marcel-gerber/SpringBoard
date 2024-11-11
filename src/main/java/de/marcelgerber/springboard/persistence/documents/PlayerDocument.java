package de.marcelgerber.springboard.persistence.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a player in a MongoBD database
 */
@Document("players")
@Data
public class PlayerDocument {

    @Id
    private String id;
    private String username;
    private String password;

    public PlayerDocument(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
