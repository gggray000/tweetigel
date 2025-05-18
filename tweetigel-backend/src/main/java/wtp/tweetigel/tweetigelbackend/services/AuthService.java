package wtp.tweetigel.tweetigelbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;

import java.util.Base64;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class AuthService {

    public static final String SESSION_USER_NAME = "tweetigel-session-user-name";
    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @Autowired
    public AuthService (BCryptPasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public User logIn(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String decoded = new String(
                    Base64.getDecoder().decode(
                        authHeader.substring(authHeader.indexOf(" ") + 1)
                    ), UTF_8
            ) ;
            String[] parts = decoded.split(":");
            String userName = parts[0];
            String password = parts[1];
            User user = userRepository.findByUsername(userName).orElseThrow();
            if (!passwordEncoder.matches(password, user.getHashedPassword())) {
                throw ClientErrors.invalidCredentials();
            }
            request.getSession().setAttribute(SESSION_USER_NAME, userName);
            return user;
        } catch (Exception e) {
            logOut(request);
            throw ClientErrors.invalidCredentials();
        }
    }

    public void logOut(HttpServletRequest request){
        if(request.getSession().getAttribute(SESSION_USER_NAME) == null){
            throw ClientErrors.invalidLogOutRequest();
        }
        request.getSession().setAttribute(SESSION_USER_NAME, null);
    }

    public User getAuthenticatedUser(HttpServletRequest request){
        Object sessionUsername = request.getSession().getAttribute(SESSION_USER_NAME);
        Optional<User> userOp = userRepository.findByUsername(sessionUsername.toString());
        if (userOp.isPresent()){
            return userOp.get();
        } else {
            throw ClientErrors.userNotFound();
        }
    }
}
