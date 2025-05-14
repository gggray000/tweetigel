package wtp.tweetigel.tweetigelbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import wtp.tweetigel.tweetigelbackend.controllers.UserController;
import wtp.tweetigel.tweetigelbackend.dtos.UserBriefDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserCreateDto;
import wtp.tweetigel.tweetigelbackend.dtos.UserLoginDto;
import wtp.tweetigel.tweetigelbackend.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest extends UserControllerTestBase{

    @Autowired
    private UserController controller;
    @Autowired
    private UserService userService;

//    @BeforeEach
//    @Override
//    public void beforeEach(){
//        super.beforeEach();
//    }

    @Test
    public void registerUser(){
        UserCreateDto userCreateDto1 = new UserCreateDto("testUser", "test123");
        UserBriefDto createdTestUser = controller.registerNewUser(testUser(), userCreateDto1);
        assertEquals("testUser", createdTestUser.username());
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
        UserCreateDto userCreateDto1 = new UserCreateDto("testUser", "test123");
        controller.registerNewUser(testUser(), userCreateDto1);
        UserLoginDto testUserWrongPassword = new UserLoginDto("testUser", "test456");
        assertThrows(
                ResponseStatusException.class, () -> controller.login(testUserWrongPassword(), testUserWrongPassword)
        );
        UserLoginDto testUserWrongUsername = new UserLoginDto("testUser1", "test123");
        assertThrows(
                ResponseStatusException.class, () -> controller.login(testUserWrongUsername(), testUserWrongUsername)
        );
        UserLoginDto testUserLoginDto = new UserLoginDto("testUser", "test123");
        assertTrue(userService.isCredentialValid(testUserLoginDto));
    }
}
