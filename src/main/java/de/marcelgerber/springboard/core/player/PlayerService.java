package de.marcelgerber.springboard.core.player;

import de.marcelgerber.springboard.exceptions.BadRequestException;
import de.marcelgerber.springboard.exceptions.NotFoundException;
import de.marcelgerber.springboard.persistence.PlayerRepository;
import de.marcelgerber.springboard.persistence.documents.PlayerDocument;
import de.marcelgerber.springboard.util.JwtUtil;
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
        return playerDocument.orElseThrow(() -> new NotFoundException("Player could not be found with ID: " + playerId));
    }

    /**
     * Finds a player by username and returns it if exists
     *
     * @param username String
     * @return PlayerDocument
     */
    public PlayerDocument getPlayerByUsername(String username) {
        Optional<PlayerDocument> playerDocument = playerRepository.findByUsername(username);
        return playerDocument.orElseThrow(() -> new NotFoundException("Player could not be found with username: " + username));
    }

    /**
     * Signs up a new player
     *
     * @param username String
     * @param password String
     * @return PlayerDocument
     */
    public PlayerDocument signupPlayer(String username, String password) {
        if(existsByUsername(username)) throw new BadRequestException("Username is already taken");

        String encodedPassword = passwordEncoder.encode(password);
        PlayerDocument playerDocument = new PlayerDocument(username, encodedPassword);
        return playerRepository.save(playerDocument);
    }

    /**
     * Returns a JWT if the provided username and password is correct
     *
     * @param username String
     * @param password String
     * @return JWT as String
     */
    public String loginPlayer(String username, String password) {
        if(!existsByUsername(username)) throw new BadRequestException("Wrong username or password");

        PlayerDocument playerDocument = getPlayerByUsername(username);

        if(!passwordEncoder.matches(password, playerDocument.getPassword())) {
            throw new BadRequestException("Wrong username or password");
        }

        return JwtUtil.generateToken(playerDocument.getUsername());
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
