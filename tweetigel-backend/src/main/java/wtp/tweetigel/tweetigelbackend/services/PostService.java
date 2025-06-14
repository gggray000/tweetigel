package wtp.tweetigel.tweetigelbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtp.tweetigel.tweetigelbackend.dtos.CommentDto;
import wtp.tweetigel.tweetigelbackend.dtos.PostDto;
import wtp.tweetigel.tweetigelbackend.entities.Comment;
import wtp.tweetigel.tweetigelbackend.entities.Post;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.CommentRepository;
import wtp.tweetigel.tweetigelbackend.repositories.PostRepository;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private AuthService authService;
    private UserService userService;
    private DateTimeFormatter timeFormatter;


    @Autowired
    public PostService(PostRepository postRepository,
                       CommentRepository commentRepository,
                       AuthService authService,
                       UserService userService){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.authService = authService;
        this.userService = userService;
        this.timeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").withZone(ZoneId.systemDefault());
    }

    private PostDto toDto(Post post, User user){
        return new PostDto(
                post.getId(),
                post.getContent(),
                userService.toUsernameDto(post.getAuthor()),
                timeFormatter.format(post.getTimestamp()),
                post.getLikedList().size(),
                post.getLikedList().contains(user)? Boolean.FALSE
                        : post.getAuthor().equals(user)? null
                            : Boolean.TRUE,
                post.getComments().size()
        );
    }

    private CommentDto toCommentDto(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getAuthor().getUsername(),
                comment.getContent(),
                timeFormatter.format(comment.getTimestamp())
        );
    }

    public void createPost(HttpServletRequest request, String content){
        User user = authService.getAuthenticatedUser(request);
        Post newPost = new Post(content, user);
        postRepository.save(newPost);
    }

    @Transactional // Without this, "TransactionRequiredException" will be thrown.
    public void deletePost(HttpServletRequest request, Long postId){
        User user = authService.getAuthenticatedUser(request);
        for (Post post: user.getPosts()){
            if(Objects.equals(post.getId(), postId)){
                postRepository.deletePostById(postId);
                user.getPosts().remove(post);
                return;
            }
        }
        throw ClientErrors.invalidDeleteRequest();
    }

    public void likePost(User user, long id) {
        Post post = postRepository.findById(id).orElseThrow(ClientErrors::postNotFound);
        if(post.getAuthor().equals(user) || post.getLikedList().contains(user)){
            throw ClientErrors.notLikable();
        }
        post.getLikedList().add(user);
        postRepository.save(post);
    }

    public void unlikePost(User user, long id) {
        Post post = postRepository.findById(id).orElseThrow(ClientErrors::postNotFound);
        if(post.getAuthor().equals(user) || !post.getLikedList().contains(user)){
            throw ClientErrors.notUnlikable();
        }
        post.getLikedList().remove(user);
        postRepository.save(post);
    }

    public List<PostDto> getPostsForProfile(HttpServletRequest request, String username, int num){
        User user = authService.getAuthenticatedUser(request);
        User toBeViewed = userService.getUser(username);
        Pageable mostRecentTwentyPosts = PageRequest.of(num,20);
        Page<Post> postsPage = postRepository.findPostsByAuthorOrderByTimestampDesc(toBeViewed, mostRecentTwentyPosts);
        return postsPage
                .get()
                .map(post -> this.toDto(post, user))
                .toList();
    }

    public int getPostsCountForProfile(String username){
        User toBeViewed = userService.getUser(username);
        return postRepository.countPostByAuthor(toBeViewed);
    }

    public List<PostDto> getPostsFeed(HttpServletRequest request, int num){
        User user = authService.getAuthenticatedUser(request);
        Pageable pageWithTwentyPosts = PageRequest.of(num, 20, Sort.by("timestamp").descending());
        Page<Post> postPage = postRepository.findPostByAuthorIsIn(user.getFollowed(), pageWithTwentyPosts);
        return postPage
                .get()
                .map(post -> this.toDto(post, user))
                .toList();
    }

    public int getPostsCountForFeed(HttpServletRequest request){
        User user = authService.getAuthenticatedUser(request);
        return postRepository.countPostByAuthorIsIn(user.getFollowed());
    }

    public void addComment(long postId, HttpServletRequest request, String content){
        Post post = postRepository.findById(postId).orElseThrow(ClientErrors::postNotFound);
        User user = authService.getAuthenticatedUser(request);
        Comment comment = new Comment(post, user, content);
        commentRepository.save(comment);
        if(!post.getComments().contains(comment)) {
            post.getComments().add(comment);
        } else throw ClientErrors.invalidCommentRequest();
        postRepository.save(post);
    }

    public List<CommentDto> getCommentsOfPost(long postId){
        Post post = postRepository.findById(postId).orElseThrow(ClientErrors::postNotFound);
        return post.getComments().stream()
                .map(this::toCommentDto)
                .toList();
    }
}
