package wtp.tweetigel.tweetigelbackend.dtos;

import java.time.Instant;

public record UserInfoConfirmDto(String id,
                                 String username,
                                 Instant registeredAt) {
}
