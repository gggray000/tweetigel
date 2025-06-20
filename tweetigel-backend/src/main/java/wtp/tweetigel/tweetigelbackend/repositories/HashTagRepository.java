package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wtp.tweetigel.tweetigelbackend.entities.HashTag;

import java.util.Optional;

@Repository
public interface HashTagRepository extends CrudRepository<HashTag, Long> {
    Optional<HashTag> findHashTagByName(String name);
    Boolean existsHashTagByName(String name);
}
