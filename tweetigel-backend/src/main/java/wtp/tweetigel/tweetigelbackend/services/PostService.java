package wtp.tweetigel.tweetigelbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.dtos.PostDto;
import wtp.tweetigel.tweetigelbackend.entities.Post;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.PostRepository;



import java.util.List;

@Service
public class PostService {

    private PostRepository postRepository;
    private AuthService authService;
    private UserService userService;


    @Autowired
    public PostService(PostRepository postRepository, AuthService authService, UserService userService){
        this.postRepository = postRepository;
        this.authService = authService;
        this.userService = userService;
    }

    private PostDto toDto(Post post){
        return new PostDto(
                post.getId(),
                post.getContent(),
                userService.toUsernameDto(post.getAuthor()),
                post.getTimestamp(),
                post.getLikedList().size()
        );
    }

    public void createPost(HttpServletRequest request, String content){
        User user = authService.getAuthenticatedUser(request);
        Post newPost = new Post(content, user);
        postRepository.save(newPost);
    }

    public void likePost(User user, long id) {
        Post post = postRepository.findById(id).orElseThrow(ClientErrors::psotNotFound);
        if(post.getAuthor().equals(user) || post.getLikedList().contains(user)){
            throw ClientErrors.notLikable();
        }
        post.getLikedList().add(user);
        postRepository.save(post);
    }

    public List<PostDto> getPostsList(String username){
        User user = userService.getUser(username);
        Pageable mostRecentTwentyPosts = PageRequest.of(0,20);
        Page<Post> postsPage = postRepository.findPostsByAuthorOrderByTimestampDesc(user, mostRecentTwentyPosts);
        return postsPage
                .get()
                .map(this::toDto)
                .toList();
    }

    public List<PostDto> getPostsFeed(){
        Pageable mostRecentTwentyPosts = PageRequest.of(0, 20, Sort.by("timestamp").descending());
        Page<Post> postPage = postRepository.findAll(mostRecentTwentyPosts);
        return postPage
                .get()
                .map(this::toDto)
                .toList();
    }
}
