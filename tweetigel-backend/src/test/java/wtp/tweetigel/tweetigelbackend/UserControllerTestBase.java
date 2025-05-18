package wtp.tweetigel.tweetigelbackend;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static wtp.tweetigel.tweetigelbackend.services.AuthService.SESSION_USER_NAME;

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

    protected MockHttpServletRequest mockRequestWithAuthHeader(String username, String password){
        MockHttpServletRequest request = new MockHttpServletRequest();
        String credential       = username + ":" + password;
        String encodedCredential = Base64.getEncoder()
                .encodeToString(credential.getBytes(StandardCharsets.UTF_8));
        String authHeader  = "Basic " + encodedCredential;
        request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
        return  request;
    }

    protected MockHttpServletRequest mockRequestWithSession(String username){
        MockHttpServletRequest  request = new MockHttpServletRequest();
        request.getSession(true).setAttribute(SESSION_USER_NAME, username);
        return request;
    }

    protected HttpServletRequest testUser(){
        return mockRequest("testUser", "test123");
    }

    protected HttpServletRequest testUserLogin(){
        return mockRequestWithAuthHeader("testUser", "test123");
    }

    protected HttpServletRequest testInvalidLogout(){
        return mockRequestWithSession("");
    }


    protected HttpServletRequest testUser2(){
        return mockRequest("testUser", "test123");
    }

    protected HttpServletRequest testUserBlank(){
        return mockRequest(" ", "");
    }

    protected HttpServletRequest testUserWrongUsername(){
        return mockRequestWithAuthHeader("testUser1", "test123");
    }

    protected HttpServletRequest testUserWrongPassword(){
        return mockRequestWithAuthHeader("testUser", "test456");
    }

    protected HttpServletRequest superStar() { return mockRequest("superStar", "test123"); }

    protected HttpServletRequest superStar2() { return mockRequest("superStar2", "test123"); }

}
