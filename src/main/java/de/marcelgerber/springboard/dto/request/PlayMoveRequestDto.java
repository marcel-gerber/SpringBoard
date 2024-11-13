package de.marcelgerber.springboard.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlayMoveRequestDto {

    @NotEmpty(message = "Key 'move' is required")
    String move;

}
