package wtp.tweetigel.tweetigelbackend.dtos;

import java.util.List;
import java.time.Instant;

public record UserDto(String id,
                      String username,
                      Instant registeredAt,
                      List<TweetDto> tweets,
                      List<UserDto> followed,
                      List<UserDto> followers) {
}
