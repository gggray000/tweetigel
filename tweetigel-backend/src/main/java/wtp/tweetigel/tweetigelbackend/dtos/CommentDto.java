package wtp.tweetigel.tweetigelbackend.dtos;

public record CommentDto(long id,
                         String author,
                         String content,
                         String timestamp) {
}
