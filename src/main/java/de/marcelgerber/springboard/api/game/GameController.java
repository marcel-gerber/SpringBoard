package de.marcelgerber.springboard.api.game;

import de.marcelgerber.springboard.api.game.dto.CreateGameDto;
import de.marcelgerber.springboard.core.GameService;
import de.marcelgerber.springboard.persistence.documents.GameDocument;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * GET /api/games
     * Retrieves all games
     *
     * @return
     */
    @GetMapping
    public String getGames() {
        return "called GET /api/games";
    }

    /**
     * GET /api/games/{gameId}
     * Retrieves a status of a specific game
     *
     * @param gameId
     * @return
     */
    @GetMapping("/{gameId}")
    public String getGameStatus(@PathVariable int gameId) {
        return "called GET /api/games/" + gameId;
    }

    /**
     * POST /api/games
     * Creates a new game
     *
     * @return ResponseEntity with GameDocument
     */
    @PostMapping()
    public ResponseEntity<GameDocument> createGame(@Valid @RequestBody CreateGameDto createGameDto) {
        return ResponseEntity.ok(gameService.createGame());
    }

    /**
     * POST /api/games/{gameId}
     * Joins an existing game
     *
     * @param gameId
     * @return
     */
    @PostMapping("/{gameId}")
    public String joinGame(@PathVariable int gameId) {
        return "called POST /api/games/" + gameId;
    }

    /**
     * POST /api/games/{gameId}/move
     * Plays a move in a specific game
     *
     * @param gameId
     * @return
     */
    @PostMapping("/{gameId}/move")
    public String playMove(@PathVariable int gameId) {
        return "called POST /api/games/" + gameId + "/move";
    }

    /**
     * GET /api/games/{gameId}/moves
     * Retrieves all moves made in a specific game
     *
     * @param gameId
     * @return
     */
    @GetMapping("/{gameId}/moves")
    public String getMoves(@PathVariable int gameId) {
        return "called GET /api/games/" + gameId + "/moves";
    }

}
