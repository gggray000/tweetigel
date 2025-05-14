package wtp.tweetigel.tweetigelbackend;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;

import java.net.http.HttpRequest;

public abstract class UserControllerTestBase {

//    UserRepository userRepository;
//
//    @BeforeEach
//    public void beforeEach(){
//        createUser("testUser", "test123");
//    }
//
//    private void createUser(String username, String password){
//        this.userRepository.save(new User(username, password));
//    }

    protected MockHttpServletRequest mockRequest(String username, String password){
        return new MockHttpServletRequest(username, password);
    }

    protected HttpServletRequest testUser(){
        return mockRequest("testUser", "test123");
    }

    protected HttpServletRequest testUser2(){
        return mockRequest("testUser", "test123");
    }

    protected HttpServletRequest testUserBlank(){
        return mockRequest(" ", "");
    }

    protected HttpServletRequest testUserWrongUsername(){
        return mockRequest("testUser1", "test123");
    }

    protected HttpServletRequest testUserWrongPassword(){
        return mockRequest("testUser", "test456");
    }

}
