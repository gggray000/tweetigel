package wtp.tweetigel.tweetigelbackend;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import wtp.tweetigel.tweetigelbackend.controllers.UserController;
import wtp.tweetigel.tweetigelbackend.dtos.*;
import wtp.tweetigel.tweetigelbackend.services.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static wtp.tweetigel.tweetigelbackend.services.AuthService.SESSION_USER_NAME;

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
        UserInfoConfirmDto response = controller.login(testUserRequestWithAuth());
        assertEquals("testUser", response.username());

        MockHttpServletRequest duplicatedLoginRequest = mockRequestWithAuthHeader("testUser", "test123");
        duplicatedLoginRequest.getSession(true).setAttribute(SESSION_USER_NAME, "testUser");
        assertThrows(ResponseStatusException.class, () -> controller.login(duplicatedLoginRequest));

        assertThrows(ResponseStatusException.class, () -> controller.login(testUserWrongPassword()));
        assertThrows(ResponseStatusException.class, () -> controller.login(testUserWrongUsername()));
    }

    @Test
    public void userLogOut(){
        controller.login(testUserRequestWithAuth());
        assertThrows(ResponseStatusException.class, () -> controller.logOut(testInvalidLogout()));
        assertDoesNotThrow(() -> controller.logOut(testLogOut()));
    }

    @Test
    public void follow(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStarDto);

        controller.follow(testUserWithSession(), new UsernameDto("superStar"));
        assertEquals(1, controller.getFollowers(new UsernameDto("superStar")).size());
        assertEquals(1, controller.getFollowedList(new UsernameDto("testUser")).size());

        assertThrows(
               ResponseStatusException.class, () -> controller.follow(testUserWithSession(), new UsernameDto("superStar"))
        );
        assertThrows(
                ResponseStatusException.class, () -> controller.follow(testUserWithSession(), new UsernameDto("superStar2"))
        );
        assertThrows(
                ResponseStatusException.class, () -> controller.follow(testUserWithSession(), new UsernameDto("testUser"))
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
        controller.follow(testUserWithSession(), superStar);
        controller.follow(testUserWithSession(), superStar2);
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
                ResponseStatusException.class, () -> controller.unfollow(testUserWithSession(), new UsernameDto("superStar3"))
        );

        assertThrows(
                ResponseStatusException.class, () -> controller.unfollow(testUserWithSession(), new UsernameDto("testUser"))
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

        List<UserSearchResultDto> searchResultDtoList = controller.searchUser(testUserWithSession(), "s");
        System.out.println(searchResultDtoList);
        assertEquals(2, searchResultDtoList.size());
        assertFalse(searchResultDtoList.get(1).followed());

        controller.unfollow(testUserWithSession(), superStar);
        searchResultDtoList = controller.searchUser(testUserWithSession(), "s");
        System.out.println(searchResultDtoList);
        assertFalse(searchResultDtoList.getFirst().followed());
    }

    @Test
    public void updateUserProfile(){
        assertNull(userService.getUser("testUser").getFullName());
        assertNull(userService.getUser("testUser").getEmail());
        assertNull(userService.getUser("testUser").getBiography());
        UserProfileUpdateDto profileUpdateDto = new UserProfileUpdateDto(
                "Test User",
                "test@test.de",
                "I am here to help!"
        );
        controller.updateUserProfile(testUserWithSession(), profileUpdateDto);
        assertEquals("Test User", userService.getUser("testUser").getFullName());
        assertEquals("test@test.de", userService.getUser("testUser").getEmail());
        assertEquals("I am here to help!", userService.getUser("testUser").getBiography());
    }

    @Test
    public void getUserProfile(){
        UserCreateDto superStarDto = new UserCreateDto("superStar", "test123");
        controller.registerNewUser(superStarDto);
        UserProfileDto profileDto = controller.getUserProfile(testUserWithSession(), "superStar");
        assertFalse((boolean) profileDto.followed());

        controller.follow(testUserWithSession(), new UsernameDto("superStar"));
        UserProfileDto updatedProfileDto = controller.getUserProfile(testUserWithSession(), "superStar");
        assertTrue((boolean) updatedProfileDto.followed());

        UserProfileDto selfProfileDto = controller.getUserProfile(testUserWithSession(), "testUser");
        assertNull(selfProfileDto.followed());
    }

}
