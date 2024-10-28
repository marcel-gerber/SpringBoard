package de.marcelgerber.springboard.core;

import de.marcelgerber.springboard.core.chesslogic.Color;
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

}
