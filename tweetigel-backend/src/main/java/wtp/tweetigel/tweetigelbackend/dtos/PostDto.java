package wtp.tweetigel.tweetigelbackend.dtos;

import java.time.Instant;
import java.util.List;

public record PostDto(long id,
                      String content,
                      UsernameDto author,
                      Instant timestamp,
                      int likesCount) {
}
