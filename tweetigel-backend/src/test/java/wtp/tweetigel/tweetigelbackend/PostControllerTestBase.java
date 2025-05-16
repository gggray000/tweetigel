package wtp.tweetigel.tweetigelbackend;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import static wtp.tweetigel.tweetigelbackend.services.AuthService.SESSION_USER_NAME;

public abstract class PostControllerTestBase {

    protected MockHttpServletRequest mockRequest(String username, String password){
        return new MockHttpServletRequest(username, password);
    }

    protected MockHttpServletRequest mockRequestWithSession(String username){
        MockHttpServletRequest  request = new MockHttpServletRequest();
        request.getSession(true).setAttribute(SESSION_USER_NAME, username);
        return request;
    }

    protected HttpServletRequest testUser(){ return mockRequest("testUser", "test123"); }

    protected HttpServletRequest superStar() { return mockRequest("superStar", "test123"); }

}
