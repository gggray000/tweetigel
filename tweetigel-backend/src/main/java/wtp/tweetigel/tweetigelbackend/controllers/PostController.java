package wtp.tweetigel.tweetigelbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import wtp.tweetigel.tweetigelbackend.dtos.PostCreateDto;
import wtp.tweetigel.tweetigelbackend.dtos.PostDto;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.services.AuthService;
import wtp.tweetigel.tweetigelbackend.services.PostService;

import java.util.List;

@RestController
public class PostController {

    private PostService postService;
    private AuthService authService;

    @Autowired
    public PostController(PostService postService, AuthService authService){
        this.postService = postService;
        this.authService = authService;
    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/post",
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createPost(HttpServletRequest request, @RequestBody PostCreateDto postCreateDto){
        this.postService.createPost(request, postCreateDto.content());
    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/post/{id}/like")
    public void likePost(HttpServletRequest request,
                         @PathVariable("id") long postId){
        User user = authService.getAuthenticatedUser(request);
        postService.likePost(user, postId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/posts/{author}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> getPostsList(@PathVariable("author") String username){
        return postService.getPostsList(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/posts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> getPostsFeed(){
        return postService.getPostsFeed();
    }


}
