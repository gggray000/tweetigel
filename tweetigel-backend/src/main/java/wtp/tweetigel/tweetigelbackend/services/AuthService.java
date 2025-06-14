package wtp.tweetigel.tweetigelbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import wtp.tweetigel.tweetigelbackend.entities.User;
import wtp.tweetigel.tweetigelbackend.exceptions.ClientErrors;
import wtp.tweetigel.tweetigelbackend.repositories.UserRepository;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class AuthService {

    public static final String SESSION_USER_NAME = "tweetigel-session-user-name";
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Logger logger;

    @Autowired
    public AuthService (BCryptPasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.logger = LoggerFactory.getLogger("tweetIgel.AuthService");
    }

    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public User logIn(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null){
            throw ClientErrors.invalidCredentials();
        }
        String decoded = new String(
                Base64.getDecoder().decode(
                        authHeader.substring(authHeader.indexOf(" ") + 1)
                ), UTF_8
        ) ;
        String[] parts = decoded.split(":");
        String username = parts[0];
        String password = parts[1];

        if (Objects.equals(request.getSession().getAttribute(SESSION_USER_NAME), username)) {
            throw ClientErrors.duplicatedLoginRequest();
        }

        User user = userRepository.findByUsername(username).orElseThrow(ClientErrors::userNotFound);
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw ClientErrors.invalidCredentials();
        }

        logger.info("User logged in: {}", username);
        request.getSession().setAttribute(SESSION_USER_NAME, username);
        logger.info("Login Session Info: {}", request.getSession().getAttribute(SESSION_USER_NAME));
        return user;
    }

    public void logOut(HttpServletRequest request){
        if(request.getSession().getAttribute(SESSION_USER_NAME) != null){
            logger.info("Logout Session Info: {}", request.getSession().getAttribute(SESSION_USER_NAME).toString());
            request.getSession().setAttribute(SESSION_USER_NAME, null);
        }else{
            throw ClientErrors.invalidLogOutRequest();
        }
    }

    public User getAuthenticatedUser(HttpServletRequest request){
        if (request.getSession().getAttribute(SESSION_USER_NAME) == null){
            throw ClientErrors.userNotFound();
        }
        Object sessionUsername = request.getSession().getAttribute(SESSION_USER_NAME);
        Optional<User> userOp = userRepository.findByUsername(sessionUsername.toString());
        if (userOp.isPresent()){
            return userOp.get();
        } else {
            throw ClientErrors.userNotFound();
        }
    }
}
