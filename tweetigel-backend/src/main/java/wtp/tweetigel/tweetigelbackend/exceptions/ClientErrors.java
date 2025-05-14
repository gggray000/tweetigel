package wtp.tweetigel.tweetigelbackend.exceptions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ClientErrors {
    private static final Logger logger = LoggerFactory.getLogger(ClientErrors.class);

    private static ResponseStatusException log(ResponseStatusException e){
        logger.error("{}\n{}", ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));
        return  e;
    }

    public static ResponseStatusException sameUsername(String username){
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "username: " + username + " already exists."));
    }

    public static ResponseStatusException notBlankAllowed() {
        return log(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "username or password can't be blank."));
    }

    public static ResponseStatusException invalidCredentials() {
        return log(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials"));
    }
}
