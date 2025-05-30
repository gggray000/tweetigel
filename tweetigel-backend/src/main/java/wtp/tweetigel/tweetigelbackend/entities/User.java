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
    private String hashedPassword;
    private Instant registeredAt;
    @OneToMany(mappedBy = "author",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.EAGER)
    private List<Post> posts;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_followed",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private Set<User> followed;
    @ManyToMany(mappedBy = "followed", fetch = FetchType.EAGER)
    private Set<User> followers;
    private String fullName;
    private String email;
    private String biography;

    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.registeredAt = Instant.now();
        this.posts = new ArrayList<>();
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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = password;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
    }

    public List<Post> getTweets() {
        return posts;
    }

    public void setTweets(List<Post> tweets) {
        this.posts = tweets;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(this.id, user.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

}
