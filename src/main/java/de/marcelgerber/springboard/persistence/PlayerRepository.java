package de.marcelgerber.springboard.persistence;

import de.marcelgerber.springboard.persistence.documents.PlayerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<PlayerDocument, String> {

    /**
     * Finds a player by username
     *
     * @param username String
     * @return PlayerDocument
     */
    Optional<PlayerDocument> findByUsername(String username);

    /**
     * Returns 'true' when a player with the provided username exists
     *
     * @param username String
     * @return boolean
     */
    boolean existsByUsername(String username);

}
