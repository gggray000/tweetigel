package wtp.tweetigel.tweetigelbackend.dtos;

import java.util.List;
import java.time.Instant;

// For user profile page
public record UserDto(String id,
                      String username,
                      Instant registeredAt,
                      List<PostDto> tweets,
                      List<UsernameDto> followed,
                      List<UsernameDto> followers) {
}
