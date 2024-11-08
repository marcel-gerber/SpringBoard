package de.marcelgerber.springboard.api.game;

import de.marcelgerber.springboard.api.game.dto.CreateGameDto;
import de.marcelgerber.springboard.api.game.dto.JoinGameDto;
import de.marcelgerber.springboard.api.game.dto.PlayMoveDto;
import de.marcelgerber.springboard.core.event.EventService;
import de.marcelgerber.springboard.core.game.GameService;
import de.marcelgerber.springboard.exceptions.GameNotFoundException;
import de.marcelgerber.springboard.persistence.documents.GameDocument;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final EventService eventService;

    public GameController(GameService gameService, EventService eventService) {
        this.gameService = gameService;
        this.eventService = eventService;
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
            throw new GameNotFoundException(gameId);
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
     * @return ResponseEntity with GameDocument
     */
    @PostMapping("/{gameId}")
    public ResponseEntity<GameDocument> joinGame(@PathVariable String gameId,
                                                 @Valid @RequestBody JoinGameDto joinGameDto) {
        GameDocument gameDocument = gameService.joinGame(gameId, joinGameDto.getPlayername());
        if(gameDocument == null) {
            throw new GameNotFoundException(gameId);
        }
        return ResponseEntity.ok(gameDocument);
    }

    /**
     * PUT /api/games/{gameId}/moves
     * Plays a move in a specific game
     *
     * @param gameId String
     * @return ResponseEntity with GameDocument
     */
    @PutMapping("/{gameId}/moves")
    public ResponseEntity<GameDocument> playMove(@PathVariable String gameId,
                                                 @Valid @RequestBody PlayMoveDto playMoveDto) {
        GameDocument gameDocument = gameService.playMove(gameId, playMoveDto.getMove());
        if(gameDocument == null) {
            throw new GameNotFoundException(gameId);
        }
        return ResponseEntity.ok(gameDocument);
    }

    /**
     * GET /api/games/{gameId}/moves
     * Retrieves all moves made in a specific game
     *
     * @param gameId String
     * @return ResponseEntity with List of String-Moves
     */
    @GetMapping("/{gameId}/moves")
    public ResponseEntity<List<String>> getMoves(@PathVariable String gameId) {
        List<String> moves = gameService.getMoves(gameId);
        if(moves == null) {
            throw new GameNotFoundException(gameId);
        }
        return ResponseEntity.ok(moves);
    }

    /**
     * GET /api/games/{gameId}/events
     * Subscribes to a Server-Sent-Event sending updates of a game
     *
     * @param gameId String
     * @return SseEmitter
     */
    @GetMapping(path = "/{gameId}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToGameEvents(@PathVariable String gameId) {
        if(!gameService.exists(gameId)) {
            throw new GameNotFoundException(gameId);
        }
        return eventService.createEmitter(gameId);
    }

}
