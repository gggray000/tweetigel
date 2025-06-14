package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import wtp.tweetigel.tweetigelbackend.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, String> {
}
