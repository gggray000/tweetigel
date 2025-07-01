package wtp.tweetigel.tweetigelbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Lazy;
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
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedList;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hashtag_post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<HashTag> hashtags;

    public Post(String content, User author) {
        this.timestamp = Instant.now();
        this.content = content;
        this.author = author;
        this.comments = new ArrayList<>();
        this.likedList = new HashSet<>();
        this.hashtags = new ArrayList<>();
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

    public List<Comment> getComments() {return comments;}

    public List<HashTag> getHashtags() {return hashtags;}
}
