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
        controller.registerNewUser(userCreateDto1);
    }

    @Test
    public void registerUser(){
        UserCreateDto userCreateDto2 = new UserCreateDto("testUser", "test123");
        assertThrows(
                ResponseStatusException.class, () -> controller.registerNewUser(userCreateDto2)
        );
        UserCreateDto userCreateDto3 = new UserCreateDto(" ", "");
        assertThrows(
                ResponseStatusException.class, () -> controller.registerNewUser(userCreateDto3)
        );
    }

    @Test
    public void userLogin(){
        UserInfoConfirmDto response = controller.login(testUserLogin());
        assertEquals("testUser", response.username());

        assertThrows(
                ResponseStatusException.class, () -> controller.login(testUserWrongPassword())
        );

        assertThrows(
                ResponseStatusException.class, () -> controller.login(testUserWrongUsername())
        );
    }

    @Test
    public void userLogOut(){
        controller.login(testUserLogin());
        assertDoesNotThrow(
                () -> controller.logOut(mockRequestWithSession("testUser"))
        );
        assertThrows(
                ResponseStatusException.class, () -> controller.login(testInvalidLogout())
        );
    }

    @Test
    public void follow(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStarDto);

        controller.follow(mockRequestWithSession("testUser"), new UsernameDto("superStar"));
        assertEquals(1, controller.getFollowers(new UsernameDto("superStar")).size());
        assertEquals(1, controller.getFollowedList(new UsernameDto("testUser")).size());

        assertThrows(
               ResponseStatusException.class, () -> controller.follow(mockRequestWithSession("testUser"), new UsernameDto("superStar"))
        );
        assertThrows(
                ResponseStatusException.class, () -> controller.follow(mockRequestWithSession("testUser"), new UsernameDto("superStar2"))
        );
        assertThrows(
                ResponseStatusException.class, () -> controller.follow(mockRequestWithSession("testUser"), new UsernameDto("testUser"))
        );
    }

    @Test
    public void unfollow(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStarDto);
        UserCreateDto superStar2Dto = new UserCreateDto("superStar2", "test123");
        controller.registerNewUser(superStar2Dto);

        UsernameDto superStar = new UsernameDto("superStar");
        UsernameDto superStar2 = new UsernameDto("superStar2");
        controller.follow(mockRequestWithSession("testUser"), superStar);
        controller.follow(mockRequestWithSession("testUser"), superStar2);
        assertEquals(2, controller.getFollowedList(new UsernameDto("testUser")).size());

        controller.unfollow(mockRequestWithSession("testUser"), superStar);
        assertEquals(0, controller.getFollowers(new UsernameDto("superStar")).size());
        assertEquals(1, controller.getFollowers(new UsernameDto("superStar2")).size());
        assertEquals(1, controller.getFollowedList(new UsernameDto("testUser")).size());

        controller.unfollow(mockRequestWithSession("testUser"), superStar);
        assertEquals(0, controller.getFollowers(new UsernameDto("superStar")).size());
        assertEquals(1, controller.getFollowers(new UsernameDto("superStar2")).size());
        assertEquals(1, controller.getFollowedList(new UsernameDto("testUser")).size());

        assertThrows(
                ResponseStatusException.class, () -> controller.unfollow(mockRequestWithSession("testUser"), new UsernameDto("superStar3"))
        );

        assertThrows(
                ResponseStatusException.class, () -> controller.unfollow(mockRequestWithSession("testUser"), new UsernameDto("testUser"))
        );
    }

    @Test
    public void searchUsers(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStarDto);
        UserCreateDto superStar2Dto = new UserCreateDto("superStar2", "test123");
        controller.registerNewUser(superStar2Dto);

        UsernameDto superStar = new UsernameDto("superStar");
        controller.follow(mockRequestWithSession("testUser"), superStar);

        List<UserSearchResultDto> searchResultDtoList = controller.searchUser(mockRequestWithSession("testUser"), "s");
        System.out.println(searchResultDtoList);
        assertEquals(2, searchResultDtoList.size());
        assertFalse(searchResultDtoList.get(1).followed());

        controller.unfollow(mockRequestWithSession("testUser"), superStar);
        searchResultDtoList = controller.searchUser(mockRequestWithSession("testUser"), "s");
        System.out.println(searchResultDtoList);
        assertFalse(searchResultDtoList.getFirst().followed());
    }

}
