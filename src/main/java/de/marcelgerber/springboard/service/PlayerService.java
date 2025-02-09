package de.marcelgerber.springboard.service;

import de.marcelgerber.springboard.exception.BadRequestException;
import de.marcelgerber.springboard.exception.NotFoundException;
import de.marcelgerber.springboard.repository.PlayerRepository;
import de.marcelgerber.springboard.model.Player;
import de.marcelgerber.springboard.util.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Handles whole business logic for players
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlackListService blackListService;

    public PlayerService(PlayerRepository playerRepository,
                         PasswordEncoder passwordEncoder,
                         BlackListService blackListService) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.blackListService = blackListService;
    }

    /**
     * Returns all players in the database
     *
     * @return List of PlayerDocument
     */
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Finds a player by id and returns it if exists
     *
     * @param playerId String
     * @return GameDocument if present
     */
    public Player getPlayerById(String playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.orElseThrow(() -> new NotFoundException("Player could not be found with ID: " + playerId));
    }

    /**
     * Finds a player by username and returns it if exists
     *
     * @param username String
     * @return PlayerDocument
     */
    public Player getPlayerByUsername(String username) {
        Optional<Player> player = playerRepository.findByUsername(username);
        return player.orElseThrow(() -> new NotFoundException("Player could not be found with username: " + username));
    }

    /**
     * Signs up a new player
     *
     * @param username String
     * @param password String
     * @return PlayerDocument
     */
    public Player signupPlayer(String username, String password) {
        if(existsByUsername(username)) throw new BadRequestException("Username is already taken");

        String encodedPassword = passwordEncoder.encode(password);
        Player player = new Player(username, encodedPassword);
        return playerRepository.save(player);
    }

    /**
     * Returns a SingletonMap if the provided username and password is correct with the following: <br />
     * Key: JWT token <br />
     * Value: Player object
     *
     * @param username String
     * @param password String
     * @return JWT as String
     */
    public Map<String, Player> loginPlayer(String username, String password) {
        if(!existsByUsername(username)) throw new BadRequestException("Wrong username or password");

        Player player = getPlayerByUsername(username);

        if(!passwordEncoder.matches(password, player.getPassword())) {
            throw new BadRequestException("Wrong username or password");
        }

        String token = JwtUtil.generateToken(player.getId());
        return Collections.singletonMap(token, player);
    }

    /**
     * Adds the provided JWT to the black list
     *
     * @param token JWT as String
     */
    public void logoutPlayer(String token) {
        blackListService.addToken(token);
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
