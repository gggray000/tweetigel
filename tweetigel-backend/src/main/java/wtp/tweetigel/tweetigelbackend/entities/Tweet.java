package wtp.tweetigel.tweetigelbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
public class Tweet {

    @Id
    private String id;
    private String content;
    @ManyToOne
    @OnDelete(action=CASCADE)
    private User author;
    private Instant timestamp;
    @OneToMany
    private List<User> likedList;

    public Tweet(String content, User author) {
        this.timestamp = Instant.now();
        this.id = author.getId() + UUID.fromString(timestamp.toString());
        this.content = content;
        this.author = author;
        this.likedList = new ArrayList<>();
    }

    public Tweet() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<User> getLikedList() {
        return likedList;
    }

    public void setLikedList(List<User> likedList) {
        this.likedList = likedList;
    }
}
