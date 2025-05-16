package wtp.tweetigel.tweetigelbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import wtp.tweetigel.tweetigelbackend.dtos.*;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.services.AuthService;
import wtp.tweetigel.tweetigelbackend.services.UserService;

import java.util.List;


//@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService){
        this.userService = userService;
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value="/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserBriefDto registerNewUser(HttpServletRequest request,
                                        @RequestBody UserCreateDto userCreateDto){
        return userService.register(userCreateDto);
    }

    @SecurityRequirement(name = "basicAuth")
    @PostMapping(value="/user/login")
    public UserLoggedDto login(HttpServletRequest request){
        User user = authService.logIn(request);
        return userService.getUser(user.getUsername());
    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/user/logout")
    public void logOut(HttpServletRequest request) {
        authService.logOut(request);
    }


    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(
            value="/user/follow",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void follow(HttpServletRequest request,
                       @RequestBody FollowDto followDto){
        userService.follow(request, followDto);
    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(
            value="/user/unfollow",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void unfollow(HttpServletRequest request,
                         @RequestBody FollowDto followDto){
        userService.unfollow(request, followDto);
    }

    @GetMapping(
            value="/user/followedList",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UsernameDto> getFollowedList(@RequestBody UsernameDto usernameDto){
        return userService.getFollowedList(usernameDto);
    }

    @GetMapping(
            value="/user/followers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UsernameDto> getFollowers(@RequestBody UsernameDto usernameDto){
        return userService.getFollowers(usernameDto);
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping(
            value="/user/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserSearchResultDto> searchUser(HttpServletRequest request,
                                                @RequestParam("term") String term){
        return userService.searchUsers(request, term);
    }

}

//    @PostMapping(
//            value="/user/login",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public ResponseEntity<String> login(HttpServletRequest request,
//                                        @RequestBody UserLoginDto userLoginDto){
//        if(userService.isCredentialValid(userLoginDto)){
//            // TODO: to be covered by test
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body("Login successfully as: " + userLoginDto.username());
//        } else {
//            throw ClientErrors.invalidCredentials();
//        }
//    }
//}


