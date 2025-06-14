package wtp.tweetigel.tweetigelbackend.dtos;

import java.time.Instant;
import java.util.List;

public record PostDto(long id,
                      String content,
                      UsernameDto author,
                      String timestamp,
                      int likesCount,
                      Boolean likeable,
                      int commentsCount) {
}
