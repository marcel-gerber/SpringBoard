package de.marcelgerber.springboard.api.game.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JoinGameDto {

    @NotEmpty(message = "Key 'playername' is required")
    private String playername;

}
