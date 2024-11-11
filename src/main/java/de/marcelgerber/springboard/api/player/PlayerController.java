package de.marcelgerber.springboard.api.player;

import de.marcelgerber.springboard.core.player.PlayerService;
import de.marcelgerber.springboard.persistence.documents.PlayerDocument;
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
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<PlayerDocument>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    /**
     * GET /api/players/{playerId} <br>
     * Retrieves all players
     *
     * @return ResponseEntity
     */
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDocument> getPlayerById(@PathVariable String playerId) {
        PlayerDocument playerDocument = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(playerDocument);
    }

    /**
     * POST /api/players/register <br>
     * Register a new player
     *
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<?> registerPlayer() {
        return ResponseEntity.ok("POST /api/players/register");
    }

    /**
     * POST /api/players/login <br>
     * Register a new player
     *
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginPlayer() {
        return ResponseEntity.ok("POST /api/players/login");
    }

}
