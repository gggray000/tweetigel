package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wtp.tweetigel.tweetigelbackend.entities.Post;
import wtp.tweetigel.tweetigelbackend.entities.User;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, String> {
    List<Post> findByAuthor(User author);
    Optional<Post> findById(long id);
    Page<Post> findPostsByAuthorOrderByTimestampDesc(User author, Pageable pageable);
    int countPostByAuthor(User author);
    Page<Post> findPostByAuthorIsIn(Collection<User> authors, Pageable pageable);
    int countPostByAuthorIsIn(Collection<User> authors);

}
