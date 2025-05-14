package wtp.tweetigel.tweetigelbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.dtos.UserBriefDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserCreateDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserLoginDto;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.TweetRepository;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private TweetRepository tweetRepository;

    @Autowired
    public UserService(UserRepository userRepository, TweetRepository tweetRepository){
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    private User toEntity(UserCreateDto userCreateDto){
        return new User(userCreateDto.username(), userCreateDto.password());
    }

    private UserBriefDto toBriefDto(User newUser) {
        return new UserBriefDto(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getRegisteredAt()
        );
    }

    public UserBriefDto register(UserCreateDto userCreateDto){
        if(userRepository.existsByUsername(userCreateDto.username())){
            throw ClientErrors.sameUsername(userCreateDto.username());
        }
        if(userCreateDto.username().isBlank() || userCreateDto.password().isBlank()){
            throw ClientErrors.notBlankAllowed();
        }
        User newUser = toEntity(userCreateDto);
        userRepository.save(newUser);
        return toBriefDto(newUser);
    }

    public boolean isCredentialValid(UserLoginDto userLoginDto){
        if(!userRepository.existsByUsername(userLoginDto.username())){
            return false;
        }
        Optional<User> tobeVerified = userRepository.findByUsername(userLoginDto.username());
        if(tobeVerified.isPresent()){
            User user = tobeVerified.get();
            return user.getPassword().equals(userLoginDto.password());
        }
        return false;
    }
}
