package wtp.tweetigel.tweetigelbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.dtos.*;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Autowired
    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    private UserInfoConfirmDto toUserInfoConfirmDto(User newUser) {
        return new UserInfoConfirmDto(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getRegisteredAt()
        );
    }

    public UsernameDto toUsernameDto(User user) {
        return new UsernameDto(user.getUsername());
    }

    private User toEntity(UserCreateDto userCreateDto) {
        return new User(
                userCreateDto.username(),
                authService.hashPassword(userCreateDto.password())
        );
    }

    public UserProfileDto toUserProfileDto(User user, Boolean followed){
        DateTimeFormatter formatter = DateTimeFormatter
                                        .ofPattern("yyyy.MM.dd")
                                        .withZone(ZoneId.systemDefault());
        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                formatter.format(user.getRegisteredAt()),
                user.getFollowed().size(),
                user.getFollowers().size(),
                user.getFullName(),
                user.getEmail(),
                user.getBiography(),
                followed
        );
    }

    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(ClientErrors::userNotFound);
    }

    public UserInfoConfirmDto register(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.username())) {
            throw ClientErrors.sameUsername(userCreateDto.username());
        }
        if (userCreateDto.username().isBlank() || userCreateDto.password().isBlank()) {
            throw ClientErrors.notBlankAllowed();
        }
        User newUser = toEntity(userCreateDto);
        userRepository.save(newUser);
        return toUserInfoConfirmDto(newUser);
    }

    public UserInfoConfirmDto getUserInfoConfirmDto(String username) {
        return userRepository.findByUsername(username)
                .map(u -> new UserInfoConfirmDto(u.getId(), u.getUsername(),u.getRegisteredAt()))
                .orElseThrow(ClientErrors::userNotFound);
    }

    public void follow(HttpServletRequest request, UsernameDto usernameDto) {
        User follower = authService.getAuthenticatedUser(request);
        User toBeFollowed = userRepository.findByUsername(usernameDto.username())
                .orElseThrow(ClientErrors::userNotFound);

        if (follower.getFollowed().contains(toBeFollowed)) {
            throw ClientErrors.noRepeatedFollow();
        }

        if(follower.equals(toBeFollowed)){
            throw ClientErrors.invalidFollowRequest();
        }

        follower.getFollowed().add(toBeFollowed);
        userRepository.save(follower);
        toBeFollowed.getFollowers().add(follower);
        userRepository.save(toBeFollowed);
    }

    public void unfollow(HttpServletRequest request, UsernameDto usernameDto) {
        User follower = authService.getAuthenticatedUser(request);
        User toBeUnfollowed = userRepository.findByUsername(usernameDto.username())
                .orElseThrow(ClientErrors::userNotFound);
        if(follower.equals(toBeUnfollowed)){
            throw ClientErrors.invalidUnfollowRequest();
        }
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

    public UserInfoConfirmDto changePassword(UserCreateDto userCreateDto) {
        if (userCreateDto.username().isBlank() || userCreateDto.password().isBlank()) {
            throw ClientErrors.notBlankAllowed();
        }
        User user = userRepository.findByUsername(userCreateDto.username())
                .orElseThrow(ClientErrors::userNotFound);
        user.setHashedPassword(authService.hashPassword(userCreateDto.password()));
        userRepository.save(user);
        return toUserInfoConfirmDto(user);
    }

    public UserProfileDto getUserProfile(HttpServletRequest request, String username){
        User user = authService.getAuthenticatedUser(request);
        User toBeViewed = userRepository.findByUsername(username)
                .orElseThrow(ClientErrors::userNotFound);
        Boolean followed = user.equals(toBeViewed) ? null : user.getFollowed().contains(toBeViewed);
        return toUserProfileDto(toBeViewed, followed);
    }

    public UserProfileDto updateUserProfile(HttpServletRequest request,
                                            UserProfileUpdateDto userProfileUpdateDto){
        User user = authService.getAuthenticatedUser(request);
        user.setFullName(userProfileUpdateDto.fullName());
        user.setEmail(userProfileUpdateDto.email());
        user.setBiography(userProfileUpdateDto.biography());
        userRepository.save(user);
        return toUserProfileDto(user, null);
    }

}
