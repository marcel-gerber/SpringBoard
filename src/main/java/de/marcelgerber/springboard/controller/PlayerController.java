package de.marcelgerber.springboard.controller;

import de.marcelgerber.springboard.dto.request.PlayerRequestDto;
import de.marcelgerber.springboard.dto.response.LoginResponseDto;
import de.marcelgerber.springboard.dto.response.LogoutResponseDto;
import de.marcelgerber.springboard.dto.response.SessionResponseDto;
import de.marcelgerber.springboard.service.PlayerService;
import de.marcelgerber.springboard.model.Player;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * GET /api/players <br>
     * Retrieves all players
     *
     * @return ResponseEntity with List of PlayerDocuments
     */
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    /**
     * GET /api/players/{playerId} <br>
     * Retrieves all players
     *
     * @return ResponseEntity with PlayerDocument
     */
    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String playerId) {
        Player player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player);
    }

    /**
     * POST /api/players/signup <br>
     * Register a new player
     *
     * @return ResponseEntity with PlayerDocument
     */
    @PostMapping("/signup")
    public ResponseEntity<Player> signupPlayer(@Valid @RequestBody PlayerRequestDto playerRequestDto) {
        Player player = playerService.signupPlayer(playerRequestDto.getUsername(),
                playerRequestDto.getPassword());
        return ResponseEntity.ok(player);
    }

    /**
     * POST /api/players/login <br>
     * Login with credentials
     *
     * @return ResponseEntity with LoginResponseDto and a header containing a httpOnly-cookie with a JWT
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginPlayer(@Valid @RequestBody PlayerRequestDto playerRequestDto,
                                                        HttpServletResponse response) {
        String token = playerService.loginPlayer(playerRequestDto.getUsername(), playerRequestDto.getPassword());

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new LoginResponseDto("Login successful"));
    }

    /**
     * GET /api/players/session <br>
     * Validate a session
     *
     * @return ResponseEntity with SessionResponseDto
     */
    @GetMapping("/session")
    public ResponseEntity<SessionResponseDto> validateSession() {
        return ResponseEntity.ok(new SessionResponseDto(true));
    }

    /**
     * POST /api/players/logout <br>
     * Logout and make JWT invalid
     *
     * @return ResponseEntity with LogoutSessionDto
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout() {
        // TODO: Implement Black List
        return ResponseEntity.ok(new LogoutResponseDto("Logout successful"));
    }

}
