package de.marcelgerber.springboard.controller;

import de.marcelgerber.springboard.dto.request.PlayerRequestDto;
import de.marcelgerber.springboard.dto.response.LoginResponseDto;
import de.marcelgerber.springboard.service.PlayerService;
import de.marcelgerber.springboard.model.Player;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
     * Register a new player
     *
     * @return ResponseEntity with LoginResponseDto containing a JWT
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginPlayer(@Valid @RequestBody PlayerRequestDto playerRequestDto) {
        String token = playerService.loginPlayer(playerRequestDto.getUsername(), playerRequestDto.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

}
