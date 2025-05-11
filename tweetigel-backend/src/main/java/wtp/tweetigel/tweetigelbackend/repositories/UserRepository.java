package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wtp.tweetigel.tweetigelbackend.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findById(UUID id);
    boolean existsById(UUID id);
}
