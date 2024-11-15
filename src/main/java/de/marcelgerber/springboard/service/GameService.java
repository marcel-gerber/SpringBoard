package de.marcelgerber.springboard.service;

import de.marcelgerber.springboard.model.Player;
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
    private final PlayerService playerService;

    public GameService(GameRepository gameRepository, EventService eventService, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.eventService = eventService;
        this.playerService = playerService;
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
     * @return Game if present
     */
    public Game getGameById(String id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.orElseThrow(() -> new NotFoundException(id));
    }

    /**
     * Returns all games in the database
     *
     * @return List of Games
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
     * @param playerId String
     * @param stringColor String
     * @return Game
     */
    public Game createGame(String playerId, String stringColor) {
        Player player = playerService.getPlayerById(playerId);

        Color color = Color.fromString(stringColor);
        if(color == Color.NONE) color = Color.WHITE;

        Game game = new Game(color, player);
        return gameRepository.save(game);
    }

    /**
     * Plays a move in the game and updates it
     *
     * @param playerId String
     * @param gameId String
     * @param move String
     * @return Game
     */
    public Game playMove(String playerId, String gameId, String move) {
        Game game = getGameById(gameId);

        if(game.getState() != GameState.ONGOING) throw new BadRequestException("Game is not in ongoing state");

        // Initialized Board is needed for getPlayerToMove() and playMove()
        game.initializeBoard();

        Player player = playerService.getPlayerById(playerId);
        Player playerToMove = game.getPlayerToMove();

        if(!player.getId().equals(playerToMove.getId())) throw new BadRequestException("You are not the next to move");

        game.playMove(move);

        // Send move update to subscribers
        eventService.sendMoveUpdate(gameId, move);

        return gameRepository.save(game);
    }

    /**
     * Joins an existing game
     *
     * @param playerId String
     * @param gameId String
     * @return Game
     */
    public Game joinGame(String playerId, String gameId) {
        Game game = getGameById(gameId);

        if(game.getState() != GameState.WAITING_FOR_PLAYER_TO_JOIN) {
            throw new BadRequestException("Game is not waiting for player to join");
        }

        Player playerJoining = playerService.getPlayerById(playerId);
        Player playerWaiting = game.getWaitingPlayer();

        if(playerJoining.getId().equals(playerWaiting.getId())) {
            throw new BadRequestException("You already joined the game");
        }

        // Send update to all subscribers that a player has joined the game
        eventService.sendPlayerJoinedUpdate(gameId, playerJoining);

        game.setJoiningPlayerName(playerJoining);
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
