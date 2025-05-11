package wtp.tweetigel.tweetigelbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import wtp.tweetigel.tweetigelbackend.dtos.UserBriefDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserCreateDto;
import wtp.tweetigel.tweetigelbackend.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
    public UserBriefDto registerNewUser(@RequestBody UserCreateDto userCreateDto){
        return userService.register(userCreateDto);
    }
}
