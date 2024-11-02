package de.marcelgerber.springboard.core.game;

import de.marcelgerber.springboard.core.game.chesslogic.Color;
import de.marcelgerber.springboard.persistence.GameRepository;
import de.marcelgerber.springboard.persistence.documents.GameDocument;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class for handling CRUD-operations for a game
 */
@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Creates a new game, saves it in the database and returns it
     *
     * @return GameDocument
     */
    public GameDocument createGame(String stringColor, String nickname) {
        Color color = Color.fromString(stringColor);
        if(color == Color.NONE) return null;

        GameDocument gameDocument = new GameDocument(color, nickname);
        return gameRepository.save(gameDocument);
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
     * @return GameDocument if present 'null' otherwise
     */
    public GameDocument getGameById(String id) {
        Optional<GameDocument> gameDocument = gameRepository.findById(id);
        return gameDocument.orElse(null);
    }

    /**
     * Returns all games in the database
     *
     * @return List of GameDocuments
     */
    public List<GameDocument> getAllGames() {
        return gameRepository.findAll();
    }

    /**
     * Plays a move in the game and updates it
     *
     * @param id String
     * @param move String
     * @return GameDocument
     */
    public GameDocument playMove(String id, String move) {
        GameDocument gameDocument = getGameById(id);
        if(gameDocument == null) return null;

        // Convert to Game and play move
        Game game = convertToGame(gameDocument);
        game.playMove(move);

        // Update GameDocument
        gameDocument.setFen(game.getFen());
        gameDocument.setState(game.getGameState());
        gameDocument.setMoves(game.getStringMoves());

        return gameRepository.save(gameDocument);
    }

    /**
     * Returns all played moves in a game
     *
     * @param id String
     * @return List of String-Moves
     */
    public List<String> getMoves(String id) {
        GameDocument gameDocument = getGameById(id);
        if(gameDocument == null) return null;
        return gameDocument.getMoves();
    }

    /**
     * Joins an existing game
     *
     * @param id String
     * @param playerName String
     * @return GameDocument
     */
    public GameDocument joinGame(String id, String playerName) {
        GameDocument gameDocument = getGameById(id);
        if(gameDocument == null) return null;

        if(gameDocument.getState() != GameState.WAITING_FOR_PLAYER_TO_JOIN) {
            throw new IllegalStateException("Game is not waiting for player to join");
        }

        // Convert to Game and update it
        Game game = convertToGame(gameDocument);
        game.setJoiningPlayerName(playerName);
        game.setOngoing();

        // Update GameDocument
        updateDocument(gameDocument, game);
        return gameRepository.save(gameDocument);
    }

    /**
     * Converts a GameDocument to a Game
     *
     * @param gameDocument GameDocument
     * @return Game
     */
    private Game convertToGame(GameDocument gameDocument) {
        return new Game(gameDocument.getFen(), gameDocument.getState(), gameDocument.getPlayerWhite(),
                gameDocument.getPlayerBlack(), gameDocument.getMoves());
    }

    /**
     * Updates the provided GameDocument to the provided Games' state
     *
     * @param gameDocument GameDocument
     * @param game Game
     */
    private void updateDocument(GameDocument gameDocument, Game game) {
        gameDocument.setFen(game.getFen());
        gameDocument.setState(game.getGameState());
        gameDocument.setPlayerWhite(game.getPlayerWhite());
        gameDocument.setPlayerBlack(game.getPlayerBlack());
        gameDocument.setMoves(game.getStringMoves());
    }

}
