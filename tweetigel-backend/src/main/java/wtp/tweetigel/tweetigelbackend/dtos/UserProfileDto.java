package wtp.tweetigel.tweetigelbackend.dtos;

public record UserProfileDto(String id,
                             String username,
                             String registeredAt,
                             int followedNum,
                             int followersNum,
                             String fullName,
                             String email,
                             String biography,
                             Boolean followed) {
}
