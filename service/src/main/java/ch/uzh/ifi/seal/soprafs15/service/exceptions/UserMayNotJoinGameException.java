package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.User;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * This is thrown when a player tries to join a game when he is already in a game (or he is not allowed to join).
 * May not happen -> Unchecked
 */
public class UserMayNotJoinGameException extends UncheckedException {

    public UserMayNotJoinGameException(String message, Class invoker) {
        super(message, invoker, HttpStatus.FORBIDDEN);
    }

    public UserMayNotJoinGameException(User user, Class invoker) {
        this("The player may not join the game." + user.getId().toString(), invoker);
    }
}
