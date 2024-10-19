package de.marcelgerber.springboard.api.game;

import de.marcelgerber.springboard.api.game.dto.CreateGameDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

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
     * @return
     */
    @PostMapping()
    public String createGame(@Valid @RequestBody CreateGameDto createGameDto) {
        return "called POST /api/games";
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
