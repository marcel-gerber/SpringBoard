package de.marcelgerber.springboard.service;

import de.marcelgerber.springboard.util.chesslogic.Color;
import de.marcelgerber.springboard.exception.BadRequestException;
import de.marcelgerber.springboard.exception.NotFoundException;
import de.marcelgerber.springboard.repository.GameRepository;
import de.marcelgerber.springboard.model.Game;
import de.marcelgerber.springboard.util.chesslogic.GameState;
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
     * Creates a new game, saves it in the database and returns it
     *
     * @return GameDocument
     */
    public Game createGame(String stringColor, String nickname) {
        Color color = Color.fromString(stringColor);
        if(color == Color.NONE) color = Color.WHITE;

        Game game = new Game(color, nickname);
        return gameRepository.save(game);
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
     * Plays a move in the game and updates it
     *
     * @param id String
     * @param move String
     * @return GameDocument
     */
    public Game playMove(String id, String move) {
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
     * @param id String
     * @param playerName String
     * @return GameDocument
     */
    public Game joinGame(String id, String playerName) {
        Game game = getGameById(id);

        if(game.getState() != GameState.WAITING_FOR_PLAYER_TO_JOIN) {
            throw new BadRequestException("Game is not waiting for player to join");
        }

        game.setJoiningPlayerName(playerName);
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
