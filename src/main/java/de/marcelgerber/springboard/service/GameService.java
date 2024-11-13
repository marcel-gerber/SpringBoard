package de.marcelgerber.springboard.service;

import de.marcelgerber.springboard.model.Player;
import de.marcelgerber.springboard.util.chesslogic.Color;
import de.marcelgerber.springboard.exception.BadRequestException;
import de.marcelgerber.springboard.exception.NotFoundException;
import de.marcelgerber.springboard.repository.GameRepository;
import de.marcelgerber.springboard.model.Game;
import de.marcelgerber.springboard.util.chesslogic.GameState;
import de.marcelgerber.springboard.util.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

/**
 * Class for handling CRUD-operations for a game
 */
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final EventService eventService;

    public GameService(GameRepository gameRepository, EventService eventService) {
        this.gameRepository = gameRepository;
        this.eventService = eventService;
    }

    /**
     * Returns 'true' when a game with the provided id exists
     *
     * @param id String
     * @return boolean
     */
    public boolean exists(String id) {
        return gameRepository.existsById(id);
    }

    /**
     * Finds a game by id and returns it if exists
     *
     * @param id String
     * @return GameDocument if present
     */
    public Game getGameById(String id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.orElseThrow(() -> new NotFoundException(id));
    }

    /**
     * Returns all games in the database
     *
     * @return List of GameDocuments
     */
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    /**
     * Returns all played moves in a game
     *
     * @param id String
     * @return List of String-Moves
     */
    public List<String> getMoves(String id) {
        Game game = getGameById(id);
        return game.getMoves();
    }

    /**
     * Creates a new game, saves it in the database and returns it
     *
     * @param player Player
     * @param stringColor String
     * @return GameDocument
     */
    public Game createGame(Player player, String stringColor) {
        Color color = Color.fromString(stringColor);
        if(color == Color.NONE) color = Color.WHITE;

        Game game = new Game(color, player);
        return gameRepository.save(game);
    }

    /**
     * Plays a move in the game and updates it
     *
     * @param player Player
     * @param id String
     * @param move String
     * @return GameDocument
     */
    public Game playMove(Player player, String id, String move) {
        Game game = getGameById(id);

        if(game.getState() != GameState.ONGOING) throw new BadRequestException("Game is not in ongoing state");

        game.playMove(move);

        // Send move update to subscribers
        eventService.sendMoveUpdate(id, move);

        return gameRepository.save(game);
    }

    /**
     * Joins an existing game
     *
     * @param player Player
     * @param id String
     * @return GameDocument
     */
    public Game joinGame(Player player, String id) {
        Game game = getGameById(id);

        if(game.getState() != GameState.WAITING_FOR_PLAYER_TO_JOIN) {
            throw new BadRequestException("Game is not waiting for player to join");
        }

        Player playerWaiting = game.getWaitingPlayer();
        if(player.getId().equals(playerWaiting.getId())) {
            throw new BadRequestException("You already joined the game");
        }

        game.setJoiningPlayerName(player);
        game.setOngoing();

        return gameRepository.save(game);
    }

    /**
     * Returns an SseEmitter if a game with the provided gameId exists
     *
     * @param gameId String
     * @return SseEmitter
     */
    public SseEmitter subscribeToEvents(String gameId) {
        if(!exists(gameId)) throw new NotFoundException(gameId);
        return eventService.createEmitter(gameId);
    }

}
