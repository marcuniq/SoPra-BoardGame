package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.User;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * If the client tries to create a game in a situation when it is not possible. May not happen -> Unchecked.
 */
public class UserMayNotCreateGameException extends UncheckedException {

    public UserMayNotCreateGameException(String message, Class invoker) {
        super(message, invoker, HttpStatus.FORBIDDEN);
    }

    public UserMayNotCreateGameException(User user, Class invoker) {
        this("User is not allowed to create this game." + user.getId().toString(), invoker);
    }
}
