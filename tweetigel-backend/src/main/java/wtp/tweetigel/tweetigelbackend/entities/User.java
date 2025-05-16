package wtp.tweetigel.tweetigelbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNullApi;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "\"user\"") //From ChatGpt. Test case could not run without this, because "user" is a reserved keyword in H2.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;
    private Instant registeredAt;
    @OneToMany(mappedBy = "author",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.EAGER)
    private List<Tweet> tweets;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_followed",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private Set<User> followed;
    @ManyToMany(mappedBy = "followed", fetch = FetchType.EAGER)
    private Set<User> followers;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.registeredAt = Instant.now();
        this.tweets = new ArrayList<>();
        this.followed = new HashSet<>();
        this.followers = new HashSet<>();
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public Set<User> getFollowed() {
        return followed;
    }

    public void setFollowed(Set<User> followed) {
        this.followed = followed;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.id == user.id && this.username == user.username;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, username);
    }

}
