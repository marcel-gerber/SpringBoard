package de.marcelgerber.springboard.api.player;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    /**
     * GET /api/players <br>
     * Retrieves all players
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<?> getAllPlayers() {
        return ResponseEntity.ok("GET /api/players");
    }

    /**
     * POST /api/players <br>
     * Register a new player
     *
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<?> registerPlayer() {
        return ResponseEntity.ok("POST /api/players");
    }

    /**
     * POST /api/players <br>
     * Register a new player
     *
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginPlayer() {
        return ResponseEntity.ok("POST /api/players/login");
    }

}
