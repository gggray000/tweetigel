package wtp.tweetigel.tweetigelbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;
import java.util.*;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    @OnDelete(action=CASCADE)
    private User author;
    private Instant timestamp;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_liked_list",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "liked_list_id")
    )
    private Set<User> likedList;

    public Post(String content, User author) {
        this.timestamp = Instant.now();
        this.content = content;
        this.author = author;
        this.likedList = new HashSet<>();
    }

    public Post() {

    }

    public Long getId() {
        return id;
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

    public Set<User> getLikedList() {
        return likedList;
    }

    public void setLikedList(Set<User> likedList) {
        this.likedList = likedList;
    }
}
