package de.marcelgerber.springboard.persistence;

import de.marcelgerber.springboard.persistence.documents.GameDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<GameDocument, String> {

}
