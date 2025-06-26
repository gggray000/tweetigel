package wtp.tweetigel.tweetigelbackend.entities;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class HashTag {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "hashtags", fetch = FetchType.EAGER)
    private List<Post> posts;

    public HashTag(String name) {
        this.name = name;
        this.posts = new ArrayList<>();
    }

    public HashTag() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
