package de.marcelgerber.springboard.api.player;

import de.marcelgerber.springboard.api.player.dto.PlayerRequestDto;
import de.marcelgerber.springboard.core.player.PlayerService;
import de.marcelgerber.springboard.persistence.documents.PlayerDocument;
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
    public ResponseEntity<List<PlayerDocument>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    /**
     * GET /api/players/{playerId} <br>
     * Retrieves all players
     *
     * @return ResponseEntity with PlayerDocument
     */
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDocument> getPlayerById(@PathVariable String playerId) {
        PlayerDocument playerDocument = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(playerDocument);
    }

    /**
     * POST /api/players/signup <br>
     * Register a new player
     *
     * @return ResponseEntity with PlayerDocument
     */
    @PostMapping("/signup")
    public ResponseEntity<PlayerDocument> signupPlayer(@Valid @RequestBody PlayerRequestDto playerRequestDto) {
        PlayerDocument playerDocument = playerService.signupPlayer(playerRequestDto.getUsername(),
                playerRequestDto.getPassword());
        return ResponseEntity.ok(playerDocument);
    }

    /**
     * POST /api/players/login <br>
     * Register a new player
     *
     * @return ResponseEntity with JWT
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginPlayer(@Valid @RequestBody PlayerRequestDto playerRequestDto) {
        String token = playerService.loginPlayer(playerRequestDto.getUsername(), playerRequestDto.getPassword());
        return ResponseEntity.ok(token);
    }

}
