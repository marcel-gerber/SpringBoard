package de.marcelgerber.springboard.core;

import de.marcelgerber.springboard.persistence.GameRepository;
import de.marcelgerber.springboard.persistence.documents.GameDocument;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameDocument createGame() {
        GameDocument gameDocument = new GameDocument();
        return gameRepository.save(gameDocument);
    }

}
