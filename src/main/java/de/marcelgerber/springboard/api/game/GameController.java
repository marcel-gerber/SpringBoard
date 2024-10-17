package de.marcelgerber.springboard.api.game;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    /**
     * Retrieves all games
     *
     * @return
     */
    @GetMapping
    public String getGames() {
        return "called GET /api/games";
    }

    /**
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
     * Creates a new game
     *
     * @return
     */
    @PostMapping("/create")
    public String createGame() {
        return "called POST /api/games/create";
    }

    /**
     * For a player to join a game
     *
     * @param gameId
     * @return
     */
    @PostMapping("/{gameId}/join")
    public String joinGame(@PathVariable int gameId) {
        return "called POST /api/games/" + gameId + "/join";
    }

    /**
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
