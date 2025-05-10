package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.stereotype.Repository;
import wtp.tweetigel.tweetigelbackend.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<User> findById(String id);
    Boolean existsById(String id);
}
