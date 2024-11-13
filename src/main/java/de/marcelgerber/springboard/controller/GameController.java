package de.marcelgerber.springboard.controller;

import de.marcelgerber.springboard.dto.request.CreateGameRequestDto;
import de.marcelgerber.springboard.dto.request.PlayMoveRequestDto;
import de.marcelgerber.springboard.model.Player;
import de.marcelgerber.springboard.service.GameService;
import de.marcelgerber.springboard.model.Game;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * GET /api/games <br>
     * Retrieves all games
     *
     * @return ResponseEntity with List of GameDocuments
     */
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    /**
     * GET /api/games/{gameId} <br>
     * Retrieves a specific game
     *
     * @param gameId String
     * @return ResponseEntity with GameDocument
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        Game game = gameService.getGameById(gameId);
        return ResponseEntity.ok(game);
    }

    /**
     * POST /api/games <br>
     * Creates a new game
     *
     * @return ResponseEntity with GameDocument
     */
    @PostMapping()
    public ResponseEntity<Game> createGame(@AuthenticationPrincipal Player player,
                                           @RequestBody CreateGameRequestDto createGameRequestDto) {
        Game game = gameService.createGame(player, createGameRequestDto.getColor());
        return ResponseEntity.ok(game);
    }

    /**
     * POST /api/games/{gameId} <br>
     * Joins an existing game
     *
     * @param gameId String
     * @return ResponseEntity with GameDocument
     */
    @PostMapping("/{gameId}")
    public ResponseEntity<Game> joinGame(@AuthenticationPrincipal Player player,
                                         @PathVariable String gameId) {
        Game game = gameService.joinGame(player, gameId);
        return ResponseEntity.ok(game);
    }

    /**
     * PUT /api/games/{gameId}/moves <br>
     * Plays a move in a specific game
     *
     * @param gameId String
     * @return ResponseEntity with GameDocument
     */
    @PutMapping("/{gameId}/moves")
    public ResponseEntity<Game> playMove(@AuthenticationPrincipal Player player,
                                         @PathVariable String gameId,
                                         @Valid @RequestBody PlayMoveRequestDto playMoveRequestDto) {
        Game game = gameService.playMove(player, gameId, playMoveRequestDto.getMove());
        return ResponseEntity.ok(game);
    }

    /**
     * GET /api/games/{gameId}/moves <br>
     * Retrieves all moves made in a specific game
     *
     * @param gameId String
     * @return ResponseEntity with List of String-Moves
     */
    @GetMapping("/{gameId}/moves")
    public ResponseEntity<List<String>> getMoves(@PathVariable String gameId) {
        List<String> moves = gameService.getMoves(gameId);
        return ResponseEntity.ok(moves);
    }

    /**
     * GET /api/games/{gameId}/events <br>
     * Subscribes to a Server-Sent-Event sending updates of a game
     *
     * @param gameId String
     * @return SseEmitter
     */
    @GetMapping(path = "/{gameId}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToGameEvents(@PathVariable String gameId) {
        return gameService.subscribeToEvents(gameId);
    }

}
