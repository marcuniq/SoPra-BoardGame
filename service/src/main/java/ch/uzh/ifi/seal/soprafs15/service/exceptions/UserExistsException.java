package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.User;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * This happens when we want to add a user that does already exist in the Repository. This is an unchecked exception.
 *
 */
public class UserExistsException extends UncheckedException {

    public UserExistsException(String message, Class invoker){
        super(message, invoker, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public UserExistsException(User user, Class invoker) {
        this("There is already a user with this id or name. " + user.getId().toString(), invoker);
    }
}
