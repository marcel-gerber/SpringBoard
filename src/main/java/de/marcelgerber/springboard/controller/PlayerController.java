package de.marcelgerber.springboard.controller;

import de.marcelgerber.springboard.dto.request.PlayerRequestDto;
import de.marcelgerber.springboard.dto.response.LoginResponseDto;
import de.marcelgerber.springboard.dto.response.LogoutResponseDto;
import de.marcelgerber.springboard.dto.response.SessionResponseDto;
import de.marcelgerber.springboard.exception.NotFoundException;
import de.marcelgerber.springboard.service.PlayerService;
import de.marcelgerber.springboard.model.Player;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        Map<String, Player> map = playerService.loginPlayer(playerRequestDto.getUsername(), playerRequestDto.getPassword());
        Map.Entry<String, Player> entry = map.entrySet().iterator().next();

        ResponseCookie cookie = ResponseCookie.from("accessToken", entry.getKey())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new LoginResponseDto("Login successful", entry.getValue().getId()));
    }

    /**
     * GET /api/players/session <br>
     * Validate a session
     *
     * @return ResponseEntity with SessionResponseDto
     */
    @GetMapping("/session")
    public ResponseEntity<SessionResponseDto> validateSession(@AuthenticationPrincipal String playerId) {
        return ResponseEntity.ok(new SessionResponseDto(true, playerId));
    }

    /**
     * POST /api/players/logout <br>
     * Logout and make JWT invalid
     *
     * @return ResponseEntity with LogoutSessionDto
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request) {
        final String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .map(Cookie::getValue).findFirst().orElseThrow(() -> new NotFoundException("Cookie not found"));
        playerService.logoutPlayer(token);
        return ResponseEntity.ok(new LogoutResponseDto("Logout successful"));
    }

}
