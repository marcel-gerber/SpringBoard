package de.marcelgerber.springboard.api.game.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateGameDto {

    @NotEmpty(message = "playername is required")
    private String playername;

    private String color;

}
