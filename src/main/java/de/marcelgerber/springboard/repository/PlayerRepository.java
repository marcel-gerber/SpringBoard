package de.marcelgerber.springboard.repository;

import de.marcelgerber.springboard.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    /**
     * Finds a player by username
     *
     * @param username String
     * @return PlayerDocument
     */
    Optional<Player> findByUsername(String username);

    /**
     * Returns 'true' when a player with the provided username exists
     *
     * @param username String
     * @return boolean
     */
    boolean existsByUsername(String username);

}
