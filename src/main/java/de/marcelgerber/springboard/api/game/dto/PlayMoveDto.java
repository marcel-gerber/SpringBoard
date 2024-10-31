package de.marcelgerber.springboard.api.game.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlayMoveDto {

    @NotEmpty(message = "move is required")
    String move;

}
