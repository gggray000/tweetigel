package wtp.tweetigel.tweetigelbackend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import wtp.tweetigel.tweetigelbackend.dtos.PostCreateDto;
import wtp.tweetigel.tweetigelbackend.dtos.PostDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserSearchResultDto;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/post",
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createPost(HttpServletRequest request,
                           @RequestBody PostCreateDto postCreateDto){
        this.postService.createPost(request, postCreateDto.content());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/post/{id}/like")
    public void likePost(HttpServletRequest request,
                         @PathVariable("id") long postId){
        User user = authService.getAuthenticatedUser(request);
        postService.likePost(user, postId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/post/{id}/unlike")
    public void unlikePost(HttpServletRequest request,
                         @PathVariable("id") long postId){
        User user = authService.getAuthenticatedUser(request);
        postService.unlikePost(user, postId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/posts/{author}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> getPostsForProfile(HttpServletRequest request,
                                      @PathVariable("author") String username,
                                      @RequestParam("page") int num){
        return postService.getPostsForProfile(request, username, num);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/postsCount/{user}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int getPostsCountForProfile(@PathVariable("user") String username){
        return postService.getPostsCountForProfile(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/posts",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> getPostsFeed(HttpServletRequest request,
                                          @RequestParam("page") int num){
        return postService.getPostsFeed(request, num);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/postsCount",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int getPostsCountForFeed(HttpServletRequest request){
        return postService.getPostsCountForFeed(request);
    }

}
