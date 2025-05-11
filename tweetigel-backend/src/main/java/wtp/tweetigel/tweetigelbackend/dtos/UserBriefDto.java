package wtp.tweetigel.tweetigelbackend.dtos;

import java.time.Instant;

public record UserBriefDto(String id,
                           String username,
                           Instant registeredAt) {
}
