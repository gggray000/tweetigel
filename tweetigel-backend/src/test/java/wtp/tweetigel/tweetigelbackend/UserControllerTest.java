package wtp.tweetigel.tweetigelbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import wtp.tweetigel.tweetigelbackend.controllers.UserController;
import wtp.tweetigel.tweetigelbackend.dtos.*;
import wtp.tweetigel.tweetigelbackend.services.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest extends UserControllerTestBase{

    @Autowired
    private UserController controller;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeEach(){
        UserCreateDto userCreateDto1 = new UserCreateDto("testUser", "test123");
        UserBriefDto createdTestUser = controller.registerNewUser(testUser(), userCreateDto1);
    }

    @Test
    public void registerUser(){
        UserCreateDto userCreateDto2 = new UserCreateDto("testUser", "test123");
        assertThrows(
                ResponseStatusException.class, () -> controller.registerNewUser(testUser2(), userCreateDto2)
        );
        UserCreateDto userCreateDto3 = new UserCreateDto(" ", "");
        assertThrows(
                ResponseStatusException.class, () -> controller.registerNewUser(testUserBlank(), userCreateDto3)
        );
    }

    @Test
    public void userLogin(){
        UserLoginDto testUserWrongPassword = new UserLoginDto("testUser", "test456");
        assertThrows(
                ResponseStatusException.class, () -> controller.login(testUserWrongPassword())
        );
        UserLoginDto testUserWrongUsername = new UserLoginDto("testUser1", "test123");
        assertThrows(
                ResponseStatusException.class, () -> controller.login(testUserWrongUsername())
        );
    }

    @Test
    public void follow(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStar(), superStarDto);
        FollowDto testFollowDto = new FollowDto("superStar");
        controller.follow(mockRequestWithSession(), testFollowDto);
        assertEquals(1, userService.getFollowers(new UsernameDto("superStar")).size());
        assertEquals(1, userService.getFollowedList(new UsernameDto("testUser")).size());
        assertThrows(
               ResponseStatusException.class, () -> controller.follow(mockRequestWithSession(), testFollowDto)
        );
        assertThrows(
                ResponseStatusException.class, () -> controller.follow(mockRequestWithSession(), new FollowDto("superStar2"))
       );
    }

    @Test
    public void unfollow(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStar(), superStarDto);
        UserCreateDto superStar2Dto = new UserCreateDto("superStar2", "test123");
        controller.registerNewUser(superStar2(), superStar2Dto);
        FollowDto testFollowDto = new FollowDto("superStar");
        FollowDto testFollow2Dto = new FollowDto("superStar2");
        controller.follow(mockRequestWithSession(), testFollowDto);
        controller.follow(mockRequestWithSession(), testFollow2Dto);
        assertEquals(2, userService.getFollowedList(new UsernameDto("testUser")).size());
        controller.unfollow(mockRequestWithSession(), testFollowDto);
        assertEquals(1, userService.getFollowedList(new UsernameDto("testUser")).size());
        controller.unfollow(mockRequestWithSession(), testFollowDto);
        assertEquals(1, userService.getFollowedList(new UsernameDto("testUser")).size());
        assertThrows(
                ResponseStatusException.class, () -> controller.unfollow(mockRequestWithSession(), new FollowDto("superStar3"))
        );
    }

    @Test
    public void searchUsers(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStar(), superStarDto);
        UserCreateDto superStar2Dto = new UserCreateDto("superStar2", "test123");
        controller.registerNewUser(superStar2(), superStar2Dto);
        FollowDto testFollowDto = new FollowDto("superStar");
        controller.follow(mockRequestWithSession(), testFollowDto);

        List<UserSearchResultDto> searchResultDtoList = controller.searchUser(mockRequestWithSession(), "s");
        System.out.println(searchResultDtoList);
        assertEquals(2, searchResultDtoList.size());
        assertFalse(searchResultDtoList.get(1).followed());

        controller.unfollow(mockRequestWithSession(), new FollowDto("superStar"));
        searchResultDtoList = controller.searchUser(mockRequestWithSession(), "s");
        System.out.println(searchResultDtoList);
        assertFalse(searchResultDtoList.getFirst().followed());
    }
}
