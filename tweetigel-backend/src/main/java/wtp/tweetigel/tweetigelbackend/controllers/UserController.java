package wtp.tweetigel.tweetigelbackend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wtp.tweetigel.tweetigelbackend.dtos.UserBriefDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserCreateDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserLoginDto;
import wtp.tweetigel.tweetigelbackend.services.UserService;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;


//@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
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

    @PostMapping(
            value="/user/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> login(HttpServletRequest request,
                                        @RequestBody UserLoginDto userLoginDto){
        if(userService.isCredentialValid(userLoginDto)){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Login successfully as: " + userLoginDto.username());
        } else {
            throw ClientErrors.invalidCredentials();
        }
    }
}
