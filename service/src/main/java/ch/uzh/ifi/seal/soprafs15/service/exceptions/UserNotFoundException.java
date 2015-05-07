package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * This shouldn't happen -> Unchecked.
 *
 */
public class UserNotFoundException extends UncheckedException {

    public UserNotFoundException(String message, Class invoker, HttpStatus status) {
        super(message, invoker);
    }

    public UserNotFoundException(Long id, Class invoker) {
        this("The user with id " + id + " couldn't be found.", invoker, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String token, Class invoker) {
        this("The user with token " + token + " couldn't be found.", invoker, HttpStatus.NOT_FOUND);
    }

}
