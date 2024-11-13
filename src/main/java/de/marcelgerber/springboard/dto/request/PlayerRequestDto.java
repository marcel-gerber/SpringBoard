package de.marcelgerber.springboard.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlayerRequestDto {

    @NotEmpty(message = "username is required")
    private String username;

    @NotEmpty(message = "password is required")
    private String password;

}
