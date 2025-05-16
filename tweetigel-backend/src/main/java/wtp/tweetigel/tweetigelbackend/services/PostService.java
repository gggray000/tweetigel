package wtp.tweetigel.tweetigelbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.entities.Post;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.PostRepository;

@Service
public class PostService {

    private PostRepository postRepository;
    private AuthService authService;

    @Autowired
    public PostService(PostRepository postRepository, AuthService authService){
        this.postRepository = postRepository;
        this.authService = authService;
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
}
