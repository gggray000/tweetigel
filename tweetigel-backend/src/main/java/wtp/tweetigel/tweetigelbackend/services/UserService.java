package wtp.tweetigel.tweetigelbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.dtos.*;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

    private UserRepository userRepository;
    private AuthService authService;


    @Autowired
    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    private UserBriefDto toBriefDto(User newUser) {
        return new UserBriefDto(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getRegisteredAt()
        );
    }

    private User toEntity(UserCreateDto userCreateDto) {
        return new User(
                userCreateDto.username(),
                authService.hashPassword(userCreateDto.password())
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

    public UserLoggedDto getUser(String username) {
        return userRepository.findByUsername(username)
                .map(u -> new UserLoggedDto(u.getId(), u.getUsername()))
                .orElseThrow(ClientErrors::userNotFound);
    }

    public void follow(HttpServletRequest request, FollowDto followDto) {
        User follower = authService.getAuthenticatedUser(request);
        User toBeFollowed = userRepository.findByUsername(followDto.username())
                .orElseThrow(ClientErrors::userNotFound);

        if (follower.getFollowed().contains(toBeFollowed)) {
            throw ClientErrors.noRepeatedFollow();
        }

        follower.getFollowed().add(toBeFollowed);
        userRepository.save(follower);
        toBeFollowed.getFollowers().add(follower);
        userRepository.save(toBeFollowed);
    }

    public void unfollow(HttpServletRequest request, FollowDto followDto) {
        User follower = authService.getAuthenticatedUser(request);
        User toBeUnfollowed = userRepository.findByUsername(followDto.username())
                .orElseThrow(ClientErrors::userNotFound);
            // if the element doesn't exist, remove() won't change anything
        follower.getFollowed().remove(toBeUnfollowed);
        userRepository.save(follower);
        toBeUnfollowed.getFollowers().remove(follower);
        userRepository.save(toBeUnfollowed);
    }

    public List<UsernameDto> getFollowedList(UsernameDto usernameDto){
        User user = userRepository.findByUsername(usernameDto.username())
                .orElseThrow(ClientErrors::userNotFound);
        return user.getFollowed()
                .stream()
                .map(u -> new UsernameDto(u.getUsername()))
                .toList();
    }

    public List<UsernameDto> getFollowers(UsernameDto usernameDto){
        User user = userRepository.findByUsername(usernameDto.username())
                .orElseThrow(ClientErrors::userNotFound);
        return user.getFollowers()
                .stream()
                .map(u -> new UsernameDto(u.getUsername()))
                .toList();
    }

    public List<UserSearchResultDto> searchUsers(HttpServletRequest request, String term){
        User user = authService.getAuthenticatedUser(request);
        List<User> resultList= userRepository.findByUsernameContainingIgnoreCase(term);
        return resultList.stream()
                .filter(result -> !result.getUsername().equals(user.getUsername()))
                .map(result ->
                    new UserSearchResultDto(
                            result.getUsername(),
                            user.getFollowed().contains(result)))
                .toList();
        }
}
