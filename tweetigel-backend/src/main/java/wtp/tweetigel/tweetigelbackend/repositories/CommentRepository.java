package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import wtp.tweetigel.tweetigelbackend.entities.Comment;
import wtp.tweetigel.tweetigelbackend.entities.Post;
import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> getCommentByPostOrderByTimestampDesc(Post post);
}
