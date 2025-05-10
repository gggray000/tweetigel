package wtp.tweetigel.tweetigelbackend.repositories;

import org.springframework.stereotype.Repository;
import wtp.tweetigel.tweetigelbackend.entities.Tweet;
import wtp.tweetigel.tweetigelbackend.entities.User;

import java.util.List;


@Repository
public interface TweetRepository {
    List<Tweet> findByAuthor(User author);
}
