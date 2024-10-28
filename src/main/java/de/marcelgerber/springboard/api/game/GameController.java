package de.marcelgerber.springboard.api.game;

import de.marcelgerber.springboard.api.game.dto.CreateGameDto;
import de.marcelgerber.springboard.core.GameService;
import de.marcelgerber.springboard.persistence.documents.GameDocument;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return ResponseEntity with List of GameDocuments
     */
    @GetMapping
    public ResponseEntity<List<GameDocument>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    /**
     * GET /api/games/{gameId}
     * Retrieves a specific game
     *
     * @param gameId String
     * @return ResponseEntity with GameDocument
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameDocument> getGame(@PathVariable String gameId) {
        GameDocument gameDocument = gameService.getGameById(gameId);
        if(gameDocument == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameDocument);
    }

    /**
     * POST /api/games
     * Creates a new game
     *
     * @return ResponseEntity with GameDocument
     */
    @PostMapping()
    public ResponseEntity<GameDocument> createGame(@Valid @RequestBody CreateGameDto createGameDto) {
        GameDocument gameDocument = gameService.createGame(createGameDto.getColor(), createGameDto.getPlayername());
        return ResponseEntity.ok(gameDocument);
    }

    /**
     * POST /api/games/{gameId}
     * Joins an existing game
     *
     * @param gameId String
     * @return
     */
    @PostMapping("/{gameId}")
    public String joinGame(@PathVariable String gameId) {
        return "called POST /api/games/" + gameId;
    }

    /**
     * POST /api/games/{gameId}/move
     * Plays a move in a specific game
     *
     * @param gameId String
     * @return
     */
    @PostMapping("/{gameId}/move")
    public String playMove(@PathVariable String gameId) {
        return "called POST /api/games/" + gameId + "/move";
    }

    /**
     * GET /api/games/{gameId}/moves
     * Retrieves all moves made in a specific game
     *
     * @param gameId String
     * @return
     */
    @GetMapping("/{gameId}/moves")
    public String getMoves(@PathVariable String gameId) {
        return "called GET /api/games/" + gameId + "/moves";
    }

}
