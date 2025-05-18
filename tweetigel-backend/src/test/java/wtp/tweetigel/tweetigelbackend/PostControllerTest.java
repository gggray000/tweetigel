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
import wtp.tweetigel.tweetigelbackend.dtos.PostCreateDto;
import wtp.tweetigel.tweetigelbackend.dtos.PostDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserCreateDto;
import wtp.tweetigel.tweetigelbackend.entities.Post;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.repositories.PostRepository;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @BeforeEach
    public void beforeEach(){
        UserCreateDto userCreateDto1 = new UserCreateDto("testUser", "test123");
        userController.registerNewUser(testUser(), userCreateDto1);

    }

    @Test
    public void createPost(){
        User testUser = userRepository.findByUsername("testUser").get();
        postController.createPost(mockRequestWithSession("testUser"), new PostCreateDto("First test post."));
        assertEquals(1, postRepository.findByAuthor(testUser).size());
    }

    @Test
    public void likePost(){
        User testUser = userRepository.findByUsername("testUser").get();
        postController.createPost(mockRequestWithSession("testUser"), new PostCreateDto("First test post."));

        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        userController.registerNewUser(superStar(), superStarDto);
        User superStar = userRepository.findByUsername("superStar").get();
        postController.createPost(mockRequestWithSession("superStar"), new PostCreateDto("Random superStar's daily."));

        Post userPost = postRepository.findByAuthor(testUser).getFirst();
        System.out.println(userPost.getId() + ", " +userPost.getAuthor());
        Post starPost = postRepository.findByAuthor(superStar).getFirst();
        System.out.println(starPost.getId() + ", " + starPost.getAuthor());

        postController.likePost(mockRequestWithSession("testUser"), starPost.getId());
        assertThrows(
                ResponseStatusException.class, () -> postController.likePost(mockRequestWithSession("testUser"), starPost.getId())
        );
        assertThrows(
                ResponseStatusException.class, () -> postController.likePost(mockRequestWithSession("testUser"), userPost.getId())
        );

    }

    @Test
    public void getPostsList(){
        User testUser = userRepository.findByUsername("testUser").get();
        postController.createPost(mockRequestWithSession("testUser"), new PostCreateDto("First test post."));
        postController.createPost(mockRequestWithSession("testUser"), new PostCreateDto("Second test post."));
        List<PostDto> postsList = postController.getPostsList("testUser");
        assertEquals(2, postsList.size());
        assertThrows(
                ResponseStatusException.class, () -> postController.getPostsList("superStar")
        );

    }




}
