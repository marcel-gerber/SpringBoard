package de.marcelgerber.springboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a player in a MongoBD database
 */
@Document("players")
@Data
public class Player {

    @Id
    private String id;
    private String username;

    @JsonIgnore
    private String password;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
