package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.User;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * If a user who is not the owner wants to start the game. May not happen -> Unchecked.
 *
 */
public class UserMayNotStartGameException extends UncheckedException {

    public UserMayNotStartGameException(User user, Class invoker) {
        super("Only the owner may start the game. The player is not the rightful owner. " + user.getId().toString(), invoker);
    }
}
