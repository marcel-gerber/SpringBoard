package de.marcelgerber.springboard.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateGameRequestDto {

    @NotEmpty(message = "Key 'playername' is required")
    private String playername;

    private String color;

}
