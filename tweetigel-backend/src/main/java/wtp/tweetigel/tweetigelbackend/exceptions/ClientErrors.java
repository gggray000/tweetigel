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
        return log(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials."));
    }

    public static ResponseStatusException duplicatedLoginRequest(){
        return log(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User already logged in, please log out first."));
    }
    public static ResponseStatusException invalidLogOutRequest() {
        return log(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to Log out."));
    }

    public static ResponseStatusException noRepeatedFollow() {
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "Already followed before."));
    }

    public static ResponseStatusException invalidFollowRequest() {
        return log(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid follow request."));
    }

    public static ResponseStatusException invalidUnfollowRequest() {
        return log(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid unfollow request."));
    }

    public static ResponseStatusException userNotFound() {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
    }

    public static ResponseStatusException postNotFound() {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));
    }

    public static ResponseStatusException invalidDeleteRequest() {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "Deletion failed."));
    }

    public static ResponseStatusException notLikable(){
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to like the post."));
    }

    public static ResponseStatusException notUnlikable(){
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to unlike the post."));
    }

    public static ResponseStatusException invalidCommentRequest(){
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to comment."));
    }

    public static ResponseStatusException hashtagNotFound(){
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "HashTag not found"));
    }

}
