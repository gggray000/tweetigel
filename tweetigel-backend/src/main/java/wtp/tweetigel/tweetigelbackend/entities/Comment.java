package wtp.tweetigel.tweetigelbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @OnDelete(action=CASCADE)
    private Post post;
    @ManyToOne
    @OnDelete(action=CASCADE)
    private User author;
    private String content;
    private Instant timestamp;

    public Comment(Post post, User author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.timestamp = Instant.now();
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }
}
