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

    protected MockHttpServletRequest mockFollowRequest(String follower, String followed){
        return new MockHttpServletRequest(follower, followed);
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

    protected HttpServletRequest superStar() { return mockRequest("superStar", "test123"); }

    protected HttpServletRequest superStar2() { return mockRequest("superStar2", "test123"); }

    protected HttpServletRequest testFollow(){ return  mockFollowRequest("testUser", "superStar"); }

    protected HttpServletRequest testFollow2(){ return  mockFollowRequest("testUser", "superStar2"); }

    protected HttpServletRequest testInvalidFollow(){ return mockFollowRequest("testUser", "superStar3"); }
}
