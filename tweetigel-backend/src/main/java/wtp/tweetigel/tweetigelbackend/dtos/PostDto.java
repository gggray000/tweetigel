package wtp.tweetigel.tweetigelbackend.dtos;

import java.time.Instant;
import java.util.List;

public record PostDto(String content, UserDto author, Instant timestamp, List<UserDto> likedList) {
}
