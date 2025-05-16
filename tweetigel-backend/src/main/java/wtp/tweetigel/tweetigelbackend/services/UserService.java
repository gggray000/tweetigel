package wtp.tweetigel.tweetigelbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.dtos.*;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.TweetRepository;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private TweetRepository tweetRepository;
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, TweetRepository tweetRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User toEntity(UserCreateDto userCreateDto) {
        return new User(
                userCreateDto.username(),
                passwordEncoder.encode(userCreateDto.password())
        );
    }

    private UserBriefDto toBriefDto(User newUser) {
        return new UserBriefDto(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getRegisteredAt()
        );
    }

    public UserBriefDto register(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.username())) {
            throw ClientErrors.sameUsername(userCreateDto.username());
        }
        if (userCreateDto.username().isBlank() || userCreateDto.password().isBlank()) {
            throw ClientErrors.notBlankAllowed();
        }
        User newUser = toEntity(userCreateDto);
        userRepository.save(newUser);
        return toBriefDto(newUser);
    }

    public boolean isCredentialValid(UserLoginDto userLoginDto) {
        if (!userRepository.existsByUsername(userLoginDto.username())) {
            return false;
        }
        Optional<User> tobeVerified = userRepository.findByUsername(userLoginDto.username());
        if (tobeVerified.isPresent()) {
            User user = tobeVerified.get();
            return passwordEncoder.matches(userLoginDto.password(), user.getHashedPassword());
        }
        return false;
    }

    public void follow(FollowDto followDto) {
        Optional<User> followerOp = userRepository.findByUsername(followDto.follower());
        Optional<User> toBeFollowedOp = userRepository.findByUsername(followDto.followed());
        if (followerOp.isPresent() && toBeFollowedOp.isPresent()) {
            User follower = followerOp.get();
            User toBeFollowed = toBeFollowedOp.get();
            if (follower.getFollowed().contains(toBeFollowed)) {
                throw ClientErrors.noRepeatedFollow();
            }
            follower.getFollowed().add(toBeFollowed);
            userRepository.save(follower);
            toBeFollowed.getFollowers().add(follower);
            userRepository.save(toBeFollowed);
        } else {
            throw ClientErrors.invalidFollowRequest();
        }
    }

    public void unfollow(FollowDto followDto) {
        Optional<User> followerOp = userRepository.findByUsername(followDto.follower());
        Optional<User> toBeUnfollowedOp = userRepository.findByUsername(followDto.followed());
        if (followerOp.isPresent() && toBeUnfollowedOp.isPresent()) {
            User follower = followerOp.get();
            User toBeUnfollowed = toBeUnfollowedOp.get();
            // if the element doesn't exist, remove() won't change anything
            follower.getFollowed().remove(toBeUnfollowed);
            userRepository.save(follower);
            toBeUnfollowed.getFollowers().remove(follower);
            userRepository.save(toBeUnfollowed);
        } else {
            throw ClientErrors.invalidUnfollowRequest();
        }
    }

    public List<UsernameDto> getFollowedList(UsernameDto usernameDto){
        Optional<User> userOp = userRepository.findByUsername(usernameDto.username());
        if(userOp.isPresent()){
            User user = userOp.get();
            List<UsernameDto> followedList = user.getFollowed().stream()
                    .map(u -> new UsernameDto(u.getUsername()))
                    .toList();
            return  followedList;
        }
        throw ClientErrors.userNotFound();
    }

    public List<UsernameDto> getFollowers(UsernameDto usernameDto){
        Optional<User> userOp = userRepository.findByUsername(usernameDto.username());
        if(userOp.isPresent()){
            User user = userOp.get();
            List<UsernameDto> followers = user.getFollowers().stream()
                    .map(u -> new UsernameDto(u.getUsername()))
                    .toList();
            return  followers;
        }
        throw ClientErrors.userNotFound();
    }
}
