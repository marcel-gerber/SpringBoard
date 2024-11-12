package de.marcelgerber.springboard.api.player.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlayerRequestDto {

    @NotEmpty(message = "username is required")
    private String username;

    @NotEmpty(message = "password is required")
    private String password;

}
