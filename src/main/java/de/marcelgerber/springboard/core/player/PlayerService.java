package de.marcelgerber.springboard.core.player;

import de.marcelgerber.springboard.exceptions.PlayerNotFoundException;
import de.marcelgerber.springboard.exceptions.UsernameAlreadyExistsException;
import de.marcelgerber.springboard.persistence.PlayerRepository;
import de.marcelgerber.springboard.persistence.documents.PlayerDocument;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Handles whole business logic for players
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public PlayerService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Returns all players in the database
     *
     * @return List of PlayerDocument
     */
    public List<PlayerDocument> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Finds a player by id and returns it if exists
     *
     * @param playerId String
     * @return GameDocument if present
     */
    public PlayerDocument getPlayerById(String playerId) {
        Optional<PlayerDocument> playerDocument = playerRepository.findById(playerId);
        return playerDocument.orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    /**
     * Finds a player by username and returns it if exists
     *
     * @param username String
     * @return PlayerDocument
     */
    public PlayerDocument getPlayerByUsername(String username) {
        Optional<PlayerDocument> playerDocument = playerRepository.findByUsername(username);
        return playerDocument.orElseThrow(() -> new PlayerNotFoundException(username));
    }

    /**
     * Registers a new player
     *
     * @param username String
     * @param password String
     * @return PlayerDocument
     */
    public PlayerDocument registerPlayer(String username, String password) {
        if(existsByUsername(username)) throw new UsernameAlreadyExistsException(username);

        String encodedPassword = passwordEncoder.encode(password);
        PlayerDocument playerDocument = new PlayerDocument(username, encodedPassword);
        return playerRepository.save(playerDocument);
    }

    /**
     * Returns 'true' when a player with the provided username exists
     *
     * @param username String
     * @return boolean
     */
    private boolean existsByUsername(String username) {
        return playerRepository.existsByUsername(username);
    }

}
