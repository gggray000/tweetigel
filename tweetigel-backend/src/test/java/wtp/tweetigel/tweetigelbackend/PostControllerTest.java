package wtp.tweetigel.tweetigelbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import wtp.tweetigel.tweetigelbackend.controllers.PostController;
import wtp.tweetigel.tweetigelbackend.controllers.UserController;
import wtp.tweetigel.tweetigelbackend.dtos.*;
import wtp.tweetigel.tweetigelbackend.entities.Post;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.repositories.HashTagRepository;
import wtp.tweetigel.tweetigelbackend.repositories.PostRepository;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;
import wtp.tweetigel.tweetigelbackend.services.PostService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostControllerTest extends PostControllerTestBase {

    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostController postController;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    public void beforeEach(){
        UserCreateDto userCreateDto1 = new UserCreateDto("testUser", "test123");
        userController.registerNewUser(userCreateDto1);
    }

    @Test
    public void createPost(){
        User testUser = userRepository.findByUsername("testUser").get();
        postController.createPost(testUserSession(), new PostCreateDto("First test post."));
        assertEquals(1, postRepository.findByAuthor(testUser).size());
    }

    @Test
    public void deletePost(){
        User testUser = userRepository.findByUsername("testUser").get();
        postController.createPost(testUserSession(), new PostCreateDto("First test post."));
        postController.createPost(testUserSession(), new PostCreateDto("Second test post."));
        assertEquals(2, postRepository.findByAuthor(testUser).size());
        assertDoesNotThrow(() -> postController.deletePost(testUserSession(), 2L));
        assertThrows(ResponseStatusException.class, () ->
                postController.deletePost(testUserSession(), 2L));
    }

    @Test
    public void likePost(){
        User testUser = userRepository.findByUsername("testUser").get();
        postController.createPost(mockRequestWithSession("testUser"), new PostCreateDto("First test post."));

        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        userController.registerNewUser(superStarDto);
        User superStar = userRepository.findByUsername("superStar").get();
        postController.createPost(mockRequestWithSession("superStar"), new PostCreateDto("Random superStar's daily."));

        Post userPost = postRepository.findByAuthor(testUser).getFirst();
        System.out.println(userPost.getId() + ", " +userPost.getAuthor());
        Post starPost = postRepository.findByAuthor(superStar).getFirst();
        System.out.println(starPost.getId() + ", " + starPost.getAuthor());

        postController.likePost(mockRequestWithSession("testUser"), starPost.getId());
        assertThrows(
                ResponseStatusException.class, () ->
                        postController.likePost(mockRequestWithSession("testUser"), starPost.getId()+1)
        );
        assertThrows(
                ResponseStatusException.class, () ->
                        postController.likePost(mockRequestWithSession("testUser"), starPost.getId())
        );
        assertThrows(
                ResponseStatusException.class, () ->
                        postController.likePost(mockRequestWithSession("testUser"), userPost.getId())
        );
    }

    @Test
    public void unlikePost(){
        User testUser = userRepository.findByUsername("testUser").get();

        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        userController.registerNewUser(superStarDto);
        User superStar = userRepository.findByUsername("superStar").get();
        postController.createPost(mockRequestWithSession("superStar"), new PostCreateDto("Another random superStar's daily."));

        Post starPost = postRepository.findByAuthor(superStar).getFirst();
        postController.likePost(mockRequestWithSession("testUser"), starPost.getId());
        assertEquals(1, postRepository.findByAuthor(superStar).getFirst().getLikedList().size());

        postController.unlikePost(mockRequestWithSession("testUser"), starPost.getId());
        assertEquals(0, postRepository.findByAuthor(superStar).getFirst().getLikedList().size());
        assertThrows(
                ResponseStatusException.class, () ->
                        postController.unlikePost(
                                mockRequestWithSession("testUser"),
                                postRepository.findByAuthor(superStar).getFirst().getId()
                        )
        );
    }

    @Test
    public void getPostsListForProfile(){
        postController.createPost(mockRequestWithSession("testUser"), new PostCreateDto("First test post."));
        postController.createPost(mockRequestWithSession("testUser"), new PostCreateDto("Second test post."));
        List<PostDto> postsList = postController.getPostsForProfile(testUserSession(), "testUser", 0);
        assertEquals(2, postsList.size());
        int postCount = postController.getPostsCountForProfile("testUser");
        assertEquals(postCount, postsList.size());
        /*assertThrows(
                ResponseStatusException.class, () -> postController.getPostsFeed(superStar(), 0)
        );*/
    }

    @Test
    public void getPostsFeed(){

        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        userController.registerNewUser(superStarDto);
        postController.createPost(mockRequestWithSession("superStar"), new PostCreateDto("Random superStar's daily."));
        postController.createPost(mockRequestWithSession("superStar"), new PostCreateDto("Another superStar's daily."));

        userController.follow(testUserSession(), new UsernameDto("superStar"));
        List<PostDto> postsList = postController.getPostsFeed(testUserSession(), 0);
        assertEquals(2, postsList.size());
        int postCount = postController.getPostsCountForFeed(testUserSession());
        assertEquals(postCount, postsList.size());
    }

    @Test
    public void addComment(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        userController.registerNewUser(superStarDto);
        postController.createPost(mockRequestWithSession("superStar"), new PostCreateDto("Random superStar's daily."));
        Post starPost = postRepository.findByAuthor(userRepository.findByUsername("superStar").get()).getFirst();

        postController.addComment(starPost.getId(), testUserSession(), new CommentCreateDto("A random comment"));
        postController.addComment(starPost.getId(), testUserSession(), new CommentCreateDto("Another random comment"));
        assertEquals(2, postController.getCommentsOfPost(starPost.getId()).size());
    }

    @Test
    public void getHashTag(){

        postController.createPost(testUserSession(), new PostCreateDto("First #test post."));
        postController.createPost(testUserSession(), new PostCreateDto("Second #test #post."));

        assertEquals(true, hashTagRepository.existsHashTagByName("test"));
        assertEquals(2, postController.getHashTag(testUserSession(), "test", 0).size());
        assertEquals(1, postController.getHashTag(testUserSession(), "post", 0).size());
        assertThrows(
                ResponseStatusException.class, () ->
                        postController.getHashTag(testUserSession(), "NonExistantHashtag", 0).size()
        );
    }

    @Test
    public void searchPost(){
        postController.createPost(testUserSession(), new PostCreateDto("First test post."));
        postController.createPost(testUserSession(), new PostCreateDto("Second test post."));
        assertEquals(1, postController.searchPosts(testUserSession(), "second", 0).size());
        postController.createPost(testUserSession(), new PostCreateDto("Third #test post."));
        assertEquals(1, postController.searchPosts(testUserSession(), "#test", 0).size());
        assertEquals(3, postController.searchPosts(testUserSession(), "test", 0).size());
    }

}
