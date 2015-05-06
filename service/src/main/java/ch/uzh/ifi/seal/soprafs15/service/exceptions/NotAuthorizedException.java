package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Marco
 */
public class NotAuthorizedException extends UncheckedException {

    public NotAuthorizedException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }

    public NotAuthorizedException(String message, Class invoker) {
        this(message, invoker, HttpStatus.UNAUTHORIZED);
    }
}
