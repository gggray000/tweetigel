package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wtp.tweetigel.tweetigelbackend.entities.Tweet;
import wtp.tweetigel.tweetigelbackend.entities.User;

import java.util.List;


@Repository
public interface TweetRepository extends CrudRepository<Tweet, String> {
    List<Tweet> findByAuthor(User author);
}
